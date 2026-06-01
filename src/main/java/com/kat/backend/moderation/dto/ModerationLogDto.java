package com.kat.backend.moderation.dto;

import com.kat.backend.moderation.entity.ModerationAction;
import com.kat.backend.moderation.entity.ModerationRuleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationLogDto {
    private UUID id;
    private String guildId;
    private String userId;
    private String username;
    private String channelId;
    private ModerationRuleType ruleType;
    private ModerationAction action;
    private String reason;
    private boolean success;
    private Instant createdAt;
}