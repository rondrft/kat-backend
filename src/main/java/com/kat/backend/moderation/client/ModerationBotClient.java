package com.kat.backend.moderation.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class ModerationBotClient {

    private final RestClient restClient;

    public ModerationBotClient(
            @Value("${internal.bot.base-url}") String botBaseUrl,
            @Value("${internal.api-key}") String apiKey
    ) {
        this.restClient = RestClient.builder()
                .baseUrl(botBaseUrl)
                .defaultHeader("X-Internal-Api-Key", apiKey)
                .build();
    }

    public void invalidateModerationCache(String guildId) {
        try {
            restClient.post()
                    .uri("/internal/guilds/{guildId}/moderation/invalidate-cache", guildId)
                    .retrieve()
                    .toBodilessEntity();
            log.debug("Invalidated moderation cache for guild {}", guildId);
        } catch (Exception e) {
            log.warn("Failed to invalidate moderation cache for guild {}: {}", guildId, e.getMessage());
        }
    }
}