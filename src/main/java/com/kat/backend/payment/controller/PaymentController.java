package com.kat.backend.payment.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.payment.dto.CreateOrderRequest;
import com.kat.backend.payment.dto.CreateOrderResponse;
import com.kat.backend.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payment/orders")
    public ResponseEntity<ApiResponse<CreateOrderResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(ApiResponse.ok(paymentService.createOrder(userId, request)));
    }

    @PostMapping("/payment/webhook")
    public ResponseEntity<Void> webhook(
            @RequestBody String body,
            @RequestHeader(value = "x-signature", required = false) String signature,
            @RequestHeader(value = "x-request-id", required = false) String requestId) {
        paymentService.processWebhook(body, signature, requestId);
        return ResponseEntity.ok().build();
    }
}
