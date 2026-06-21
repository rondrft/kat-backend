package com.kat.backend.payment.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kat.backend.payment.config.MercadoPagoProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class MercadoPagoClient {

    private final RestClient restClient;
    private final MercadoPagoProperties props;

    public MercadoPagoClient(@Qualifier("mercadoPagoRestClient") RestClient restClient,
                              MercadoPagoProperties props) {
        this.restClient = restClient;
        this.props = props;
    }

    public record PreferenceResult(String id, String initPoint) {}

    public record PaymentResult(String id, String status, String statusDetail, String externalReference) {}

    public PreferenceResult createPreference(String externalReference, String title, BigDecimal amount) {
        boolean isHttps = props.backUrlBase() != null && props.backUrlBase().startsWith("https://");
        var request = new MpPreferenceRequest(
                List.of(new MpItem("kat_premium", title, "Kat Premium — full access for your Discord server", 1, amount, props.currencyId())),
                new MpBackUrls(
                        props.backUrlBase() + "/payment/success",
                        props.backUrlBase() + "/payment/failure",
                        props.backUrlBase() + "/payment/pending"
                ),
                isHttps ? "approved" : null,
                externalReference,
                props.notificationUrl(),
                "KAT PREMIUM"
        );

        try {
            MpPreferenceResponse response = restClient.post()
                    .uri("/checkout/preferences")
                    .body(request)
                    .retrieve()
                    .body(MpPreferenceResponse.class);

            if (response == null || response.initPoint() == null) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Empty response from payment provider");
            }
            return new PreferenceResult(response.id(), response.initPoint());
        } catch (RestClientException e) {
            log.error("MercadoPago preference creation failed: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Payment provider temporarily unavailable");
        }
    }

    public PaymentResult fetchPayment(String paymentId) {
        try {
            MpPaymentResponse response = restClient.get()
                    .uri("/v1/payments/{id}", paymentId)
                    .retrieve()
                    .body(MpPaymentResponse.class);

            if (response == null) {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Empty payment response from provider");
            }
            return new PaymentResult(
                    String.valueOf(response.id()),
                    response.status(),
                    response.statusDetail(),
                    response.externalReference()
            );
        } catch (RestClientException e) {
            log.error("MercadoPago payment fetch failed for {}: {}", paymentId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Could not fetch payment details");
        }
    }

    // ---- Internal Jackson models ----

    @JsonIgnoreProperties(ignoreUnknown = true)
    record MpItem(
            String id,
            String title,
            String description,
            int quantity,
            @JsonProperty("unit_price") BigDecimal unitPrice,
            @JsonProperty("currency_id") String currencyId
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record MpBackUrls(
            String success,
            String failure,
            String pending
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    record MpPreferenceRequest(
            List<MpItem> items,
            @JsonProperty("back_urls") MpBackUrls backUrls,
            @JsonProperty("auto_return") String autoReturn,
            @JsonProperty("external_reference") String externalReference,
            @JsonProperty("notification_url") String notificationUrl,
            @JsonProperty("statement_descriptor") String statementDescriptor
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record MpPreferenceResponse(
            String id,
            @JsonProperty("init_point") String initPoint,
            @JsonProperty("sandbox_init_point") String sandboxInitPoint
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    record MpPaymentResponse(
            long id,
            String status,
            @JsonProperty("status_detail") String statusDetail,
            @JsonProperty("external_reference") String externalReference,
            @JsonProperty("transaction_amount") BigDecimal transactionAmount
    ) {}
}
