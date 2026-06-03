package com.kat.backend.audit.dto;

import lombok.Data;

@Data
public class RankingEntryDto {
    private String discordUserId;
    private String username;
    private String avatar;
    private long messageCount;
}