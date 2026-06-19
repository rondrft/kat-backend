package com.kat.backend.moderation.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationConfigDto {

    private String guildId;

    private boolean enabled;

    @Min(0) @Max(100)
    private int strictness;

    @Min(1) @Max(1440)
    private int defaultTimeoutMinutes;

    @NotNull @Valid
    private List<ModerationRuleDto> rules;

    private String logChannelId;
    private String premiumLogChannelId;
}