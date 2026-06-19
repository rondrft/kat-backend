package com.kat.backend.moderation.dto;

import com.kat.backend.moderation.entity.ModerationAction;
import com.kat.backend.moderation.entity.ModerationRuleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationAutoPunishmentDto {
    private UUID id;
    private String guildId;
    private ModerationRuleType ruleType;
    private int threshold;
    private ModerationAction action;
    private Integer timeoutMinutes;
    private boolean enabled;
}
