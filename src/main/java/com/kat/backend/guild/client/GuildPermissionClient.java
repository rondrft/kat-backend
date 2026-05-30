package com.kat.backend.guild.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

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
        try {
            Map response = restClient.get()
                    .uri("/internal/guilds/{guildId}/members/{discordId}/is-admin",
                            guildId, discordId)
                    .retrieve()
                    .body(Map.class);

            return response != null && Boolean.TRUE.equals(response.get("admin"));
        } catch (Exception e) {
            return false;
        }
    }
}