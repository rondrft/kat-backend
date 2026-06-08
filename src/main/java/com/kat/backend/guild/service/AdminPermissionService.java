package com.kat.backend.guild.service;

import com.kat.backend.guild.client.GuildPermissionClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AdminPermissionService {

    private final GuildPermissionClient guildPermissionClient;

    public AdminPermissionService(GuildPermissionClient guildPermissionClient) {
        this.guildPermissionClient = guildPermissionClient;
    }

    @Cacheable(value = "adminPermissions", key = "#guildId + ':' + #discordId")
    public boolean isAdmin(String guildId, String discordId) {
        return guildPermissionClient.isAdmin(guildId, discordId);
    }
}
