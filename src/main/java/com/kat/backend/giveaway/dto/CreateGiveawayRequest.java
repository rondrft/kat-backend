package com.kat.backend.giveaway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateGiveawayRequest {

    @NotBlank(message = "Prize is required")
    private String prize;

    @NotBlank(message = "Channel ID is required")
    private String channelId;

    @NotNull(message = "Duration in minutes is required")
    private Long durationMinutes;

    @NotNull(message = "Winner count is required")
    private Integer winnerCount;

    @NotNull(message = "Booster only flag is required")
    private Boolean boosterOnly;

    private Boolean startImmediately;
}
