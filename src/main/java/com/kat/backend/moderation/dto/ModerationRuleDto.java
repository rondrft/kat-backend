package com.kat.backend.moderation.dto;

import com.kat.backend.moderation.entity.ModerationAction;
import com.kat.backend.moderation.entity.ModerationRuleType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationRuleDto {

    @NotNull
    private ModerationRuleType id;

    private boolean enabled;

    @NotNull
    private ModerationAction action;

    @Min(1) @Max(100)
    private int threshold;
}