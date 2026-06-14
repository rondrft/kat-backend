package com.kat.backend.guild.service;

import com.kat.backend.guild.client.GuildPermissionClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminPermissionService {

    private final GuildPermissionClient guildPermissionClient;

    public AdminPermissionService(GuildPermissionClient guildPermissionClient) {
        this.guildPermissionClient = guildPermissionClient;
    }

    @Cacheable(value = "adminPermissions", key = "#guildId + ':' + #discordId")
    public boolean isAdmin(String guildId, String discordId) {
        boolean admin = guildPermissionClient.isAdmin(guildId, discordId);
        log.debug("Admin check for user {} in guild {}: {}", discordId, guildId, admin);
        return admin;
    }
}
