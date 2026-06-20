package com.kat.backend.moderation.dto;

import lombok.Data;

@Data
public class PurgeConfigDto {
    private boolean enabled;
    private String allowedRoleId;
    private int maxMessages;
    private int maxAgeSeconds;
    private boolean purgeUserEnabled;
    private String purgeUserAllowedRoleId;
    private String purgeUserAllowedUserId;
    private int purgeUserMaxMessages;
    private int purgeUserMaxAgeSeconds;
}