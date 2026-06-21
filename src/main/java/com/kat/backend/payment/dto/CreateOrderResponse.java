package com.kat.backend.payment.dto;

public record CreateOrderResponse(
        String orderId,
        String checkoutUrl
) {}
