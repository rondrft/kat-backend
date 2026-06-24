package com.kat.backend.branding.dto;

public record GuildBrandingRequest(
        String botName,
        String avatarUrl,
        String bannerUrl,
        String description
) {}
