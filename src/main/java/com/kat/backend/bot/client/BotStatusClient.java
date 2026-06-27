package com.kat.backend.bot.client;

import com.kat.backend.bot.dto.BotStatusDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class BotStatusClient {

    private final RestClient restClient;

    public BotStatusClient(@Qualifier("botRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public BotStatusDto getStatus() {
        try {
            BotStatusDto status = restClient.get()
                    .uri("/internal/bot/status")
                    .retrieve()
                    .body(BotStatusDto.class);
            log.debug("Bot status fetched: {}/{} shards connected, {} guilds",
                    status != null ? status.getConnectedShards() : 0,
                    status != null ? status.getTotalShards() : 0,
                    status != null ? status.getTotalGuilds() : 0);
            return status;
        } catch (Exception e) {
            log.error("Failed to fetch bot status from {}: {}", 
                    "/internal/bot/status", e.getMessage());
            return null;
        }
    }
}
