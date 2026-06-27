package com.kat.backend.leaderboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ServerLeaderboardEntryDto(
        String id,
        String guildId,
        String name,
        String iconUrl,
        int memberCount,
        @JsonProperty("isPublic") boolean isPublic,
        boolean showOnLeaderboard
) {}
