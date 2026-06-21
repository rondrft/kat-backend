package com.kat.backend.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mercadopago")
public record MercadoPagoProperties(
        String accessToken,
        String webhookSecret,
        String notificationUrl,
        String backUrlBase,
        String currencyId
) {}
