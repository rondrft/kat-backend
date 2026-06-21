package com.kat.backend.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateOrderRequest(
        @NotBlank String guildId,
        @NotBlank @Pattern(regexp = "monthly|yearly|lifetime") String plan
) {}
