package com.kat.backend.guild.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class GuildConfigBotClient {

    private final RestClient restClient;

    public GuildConfigBotClient(
            @Value("${internal.bot.base-url}") String botBaseUrl,
            @Value("${internal.api-key}") String apiKey
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(botBaseUrl)
                .defaultHeader("X-Internal-Api-Key", apiKey)
                .build();
    }

    public void invalidateCache(String guildId) {
        try {
            restClient.post()
                    .uri("/internal/guilds/{guildId}/config/invalidate-cache", guildId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("Failed to invalidate guild config cache for {}: {}", guildId, e.getMessage());
        }
    }
}
