package com.kat.backend.logging.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Component
public class LoggingBotClient {

    private final RestClient restClient;

    public LoggingBotClient(
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
                    .uri("/internal/guilds/{guildId}/logging/invalidate-cache", guildId)
                    .retrieve()
                    .toBodilessEntity();
            log.debug("Invalidated logging cache for guild {}", guildId);
        } catch (Exception e) {
            log.warn("Failed to invalidate logging cache for guild {}: {}", guildId, e.getMessage());
        }
    }

    public void notifyConfigSaved(String guildId, String defaultChannel) {
        try {
            restClient.post()
                    .uri("/internal/guilds/{guildId}/logging/save", guildId)
                    .body(Map.of("defaultChannel", defaultChannel != null ? defaultChannel : ""))
                    .retrieve()
                    .toBodilessEntity();
            log.debug("Notified bot of logging config save for guild {}", guildId);
        } catch (Exception e) {
            log.warn("Failed to notify bot of logging config save: {}", e.getMessage());
        }
    }
}
