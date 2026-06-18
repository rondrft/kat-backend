package com.kat.backend.moderation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SecurityScanFindingDto {

    private String id;
    private SecurityScanCategory category;
    private SecurityScanSeverity severity;
    private String title;
    private String description;
    private String targetId;
    private String targetName;
    private String recommendation;

    public enum SecurityScanCategory {
        CHANNEL,
        ROLE,
        MEMBER,
        GENERAL
    }
}
