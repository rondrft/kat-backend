package com.kat.backend.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kat.backend.guild.service.AdminPermissionService;
import com.kat.backend.payment.config.MercadoPagoProperties;
import com.kat.backend.payment.dto.CreateOrderRequest;
import com.kat.backend.payment.dto.CreateOrderResponse;
import com.kat.backend.payment.entity.PaymentOrder;
import com.kat.backend.payment.entity.PaymentStatus;
import com.kat.backend.payment.repository.PaymentOrderRepository;
import com.kat.backend.premium.service.PremiumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private static final Map<String, BigDecimal> PLAN_AMOUNTS = Map.of(
            "monthly", new BigDecimal("4.99"),
            "yearly", new BigDecimal("24.99"),
            "lifetime", new BigDecimal("44.99")
    );

    private static final Map<String, String> PLAN_TITLES = Map.of(
            "monthly", "Kat Premium — Monthly",
            "yearly", "Kat Premium — Yearly",
            "lifetime", "Kat Premium — Lifetime"
    );

    private final PaymentOrderRepository paymentOrderRepository;
    private final MercadoPagoClient mercadoPagoClient;
    private final PremiumService premiumService;
    private final AdminPermissionService adminPermissionService;
    private final MercadoPagoProperties props;
    private final ObjectMapper objectMapper;

    public CreateOrderResponse createOrder(String userId, CreateOrderRequest request) {
        String guildId = request.guildId();
        String plan = request.plan();

        if (!adminPermissionService.isAdmin(guildId, userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You must be a server admin to purchase premium");
        }

        BigDecimal amount = PLAN_AMOUNTS.get(plan);
        if (amount == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown plan: " + plan);
        }

        Optional<PaymentOrder> existing = paymentOrderRepository
                .findTopByGuildIdAndUserIdAndPlanAndStatusAndCreatedAtAfter(
                        guildId, userId, plan, PaymentStatus.PENDING, LocalDateTime.now().minusHours(1));

        if (existing.isPresent()) {
            PaymentOrder order = existing.get();
            if (order.getCheckoutUrl() != null) {
                return new CreateOrderResponse(order.getId(), order.getCheckoutUrl());
            }
        }

        String orderId = UUID.randomUUID().toString();
        PaymentOrder order = PaymentOrder.builder()
                .id(orderId)
                .guildId(guildId)
                .userId(userId)
                .plan(plan)
                .amount(amount)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        paymentOrderRepository.save(order);

        MercadoPagoClient.PreferenceResult pref = mercadoPagoClient.createPreference(
                orderId, PLAN_TITLES.get(plan), amount);

        order.setMercadoPagoPreferenceId(pref.id());
        order.setCheckoutUrl(pref.initPoint());
        order.setUpdatedAt(LocalDateTime.now());
        paymentOrderRepository.save(order);

        return new CreateOrderResponse(orderId, pref.initPoint());
    }

    @Transactional
    public void processWebhook(String body, String signature, String requestId) {
        try {
            JsonNode root = objectMapper.readTree(body);
            String type = root.path("type").asText();

            if (!"payment".equals(type)) {
                return;
            }

            String dataId = root.path("data").path("id").asText();
            if (dataId.isBlank()) {
                log.warn("Webhook missing data.id");
                return;
            }

            boolean hasSecret = props.webhookSecret() != null && !props.webhookSecret().isBlank();
            if (!hasSecret) {
                log.error("MERCADOPAGO_WEBHOOK_SECRET not configured — rejecting webhook");
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Webhook processing unavailable");
            }
            if (signature == null || signature.isBlank()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing webhook signature");
            }
            verifySignature(signature, requestId, dataId);

            if (paymentOrderRepository.findByMercadoPagoPaymentId(dataId).isPresent()) {
                log.debug("Payment {} already processed", dataId);
                return;
            }

            MercadoPagoClient.PaymentResult payment = mercadoPagoClient.fetchPayment(dataId);
            String externalRef = payment.externalReference();

            paymentOrderRepository.findById(externalRef).ifPresentOrElse(order -> {
                PaymentStatus newStatus = switch (payment.status()) {
                    case "approved" -> PaymentStatus.APPROVED;
                    case "rejected", "cancelled" -> PaymentStatus.REJECTED;
                    default -> PaymentStatus.PENDING;
                };

                order.setMercadoPagoPaymentId(dataId);
                order.setStatus(newStatus);
                order.setUpdatedAt(LocalDateTime.now());
                paymentOrderRepository.save(order);

                if (newStatus == PaymentStatus.APPROVED) {
                    premiumService.activatePremium(order.getGuildId(), order.getPlan(), order.getUserId());
                    log.info("Premium activated: guild={}, plan={}", order.getGuildId(), order.getPlan());
                }
            }, () -> log.warn("No order found for external_reference={}", externalRef));

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            log.error("Webhook processing failed: {}", e.getMessage(), e);
        }
    }

    private void verifySignature(String signature, String requestId, String dataId) {
        String ts = null;
        String v1 = null;
        for (String part : signature.split("&")) {
            if (part.startsWith("ts=")) ts = part.substring(3);
            else if (part.startsWith("v1=")) v1 = part.substring(3);
        }

        if (ts == null || v1 == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid signature format");
        }

        String template = "id:" + dataId + ";request-id:" + (requestId != null ? requestId : "") + ";ts:" + ts;
        String computed = computeHmac(props.webhookSecret(), template);

        if (!MessageDigest.isEqual(computed.getBytes(StandardCharsets.UTF_8), v1.getBytes(StandardCharsets.UTF_8))) {
            log.warn("Webhook signature mismatch for dataId={}", dataId);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Signature verification failed");
        }
    }

    private static String computeHmac(String secret, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("HMAC computation failed", e);
        }
    }
}
