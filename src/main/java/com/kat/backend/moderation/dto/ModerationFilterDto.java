package com.kat.backend.moderation.dto;

import com.kat.backend.moderation.entity.ModerationAction;
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
public class ModerationFilterDto {
    private UUID id;
    private String guildId;
    private String pattern;
    private boolean enabled;
    private ModerationAction action;
    private String replacement;
    private String reason;
    private Instant createdAt;
}
