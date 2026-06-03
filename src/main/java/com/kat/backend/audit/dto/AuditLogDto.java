package com.kat.backend.audit.dto;

import lombok.Data;
import java.time.Instant;
import java.util.UUID;

@Data
public class AuditLogDto {
    private UUID id;
    private String targetDiscordId;
    private String targetUsername;
    private String targetAvatar;
    private String executorDiscordId;
    private String executorUsername;
    private String action;
    private String reason;
    private Integer durationMinutes;
    private Instant createdAt;
}