package com.kat.backend.giveaway.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateGiveawayRequest {

    @NotBlank(message = "Prize is required")
    @Size(max = 256, message = "Prize cannot exceed 256 characters")
    private String prize;

    @NotBlank(message = "Channel ID is required")
    private String channelId;

    @NotNull(message = "Duration in minutes is required")
    @Min(value = 0, message = "Duration cannot be negative")
    @Max(value = 86400, message = "Duration cannot exceed 60 days")
    private Long durationMinutes;

    @NotNull(message = "Winner count is required")
    @Min(value = 1, message = "Winner count must be at least 1")
    @Max(value = 25, message = "Winner count cannot exceed 25")
    private Integer winnerCount;

    @NotNull(message = "Booster only flag is required")
    private Boolean boosterOnly;

    private Boolean startImmediately;
}
