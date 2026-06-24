package com.kat.backend.backup.dto;

public record BackupDto(
        String id,
        String guildId,
        String name,
        String createdAt,
        int dataSize
) {}
