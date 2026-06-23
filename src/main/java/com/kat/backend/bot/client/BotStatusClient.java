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
            return restClient.get()
                    .uri("/internal/bot/status")
                    .retrieve()
                    .body(BotStatusDto.class);
        } catch (Exception e) {
            log.error("Failed to fetch bot status: {}", e.getMessage());
            return null;
        }
    }
}
