package com.kat.backend.logging.dto;

import lombok.Data;

@Data
public class LoggingEntryDto {
    private String id;
    private boolean enabled;
    private String channelId;
}