package com.kat.backend.antiraid.dto;

public record AntiRaidConfigDto(
        String guildId,
        boolean antiRaidEnabled,
        int joinThreshold,
        int joinWindowSeconds,
        String joinAction,
        String alertChannelId,
        int minAccountAgeDays,
        boolean requireAvatar,
        String accountAction,
        boolean massBanEnabled,
        boolean massKickEnabled,
        boolean massDeleteEnabled,
        boolean massWebhookEnabled,
        int massActionThreshold,
        int massActionWindowSeconds,
        String massAction
) {}
