package com.kat.backend.leaderboard.dto;

public record LeaderboardSettingsDto(
        String guildId,
        boolean showOnLeaderboard
) {}
