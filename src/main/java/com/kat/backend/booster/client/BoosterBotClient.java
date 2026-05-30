package com.kat.backend.booster.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class BoosterBotClient {

    private final RestClient restClient;

    public BoosterBotClient(
            @Value("${internal.bot.base-url}") String botBaseUrl,
            @Value("${internal.api-key}") String apiKey) {

        this.restClient = RestClient.builder()
                .baseUrl(botBaseUrl)
                .defaultHeader("X-Internal-Api-Key", apiKey)
                .build();
    }

    public int syncBoosters(String guildId) {
        Map response = restClient.post()
                .uri("/internal/guilds/{guildId}/boosters/sync", guildId)
                .retrieve()
                .body(Map.class);

        return response != null ? (int) response.get("synced") : 0;
    }
}