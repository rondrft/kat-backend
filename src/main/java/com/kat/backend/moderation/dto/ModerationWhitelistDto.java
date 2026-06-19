package com.kat.backend.moderation.dto;

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
public class ModerationWhitelistDto {
    private UUID id;
    private String guildId;
    private String channelId;
    private String userId;
    private String categoryId;
    private ModerationRuleType ruleType;
    private String reason;
    private Instant createdAt;
}
