package com.kat.backend.guild.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Component
public class GuildPermissionClient {

    private final RestClient restClient;

    public GuildPermissionClient(
            @Value("${internal.bot.base-url}") String botBaseUrl,
            @Value("${internal.api-key}") String apiKey) {

        this.restClient = RestClient.builder()
                .baseUrl(botBaseUrl)
                .defaultHeader("X-Internal-Api-Key", apiKey)
                .build();
    }

    public boolean isAdmin(String guildId, String discordId) {
        Map response = restClient.get()
                .uri("/internal/guilds/{guildId}/members/{discordId}/is-admin",
                        guildId, discordId)
                .retrieve()
                .body(Map.class);

        return response != null && Boolean.TRUE.equals(response.get("admin"));
    }

    public boolean hasAnyRole(String guildId, String discordId, java.util.List<String> roleIds) {
        String joined = String.join(",", roleIds);
        Map response = restClient.get()
                .uri("/internal/guilds/{guildId}/members/{discordId}/has-role?roleIds={roleIds}",
                        guildId, discordId, joined)
                .retrieve()
                .body(Map.class);

        return response != null && Boolean.TRUE.equals(response.get("hasRole"));
    }
}