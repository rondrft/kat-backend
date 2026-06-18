package com.kat.backend.tickets.service;

import com.kat.backend.tickets.dto.TicketConfigResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class TicketBotClient {

    @Qualifier("botRestClient")
    private final RestClient botRestClient;

    public void createOrUpdatePanel(String guildId, TicketConfigResponse config) {
        try {
            botRestClient.post()
                    .uri("/internal/guilds/{guildId}/tickets/panel", guildId)
                    .body(config)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("Failed to create/update ticket panel for guild {}: {}", guildId, e.getMessage());
            throw new RuntimeException("Could not create ticket panel in Discord", e);
        }
    }

    public void deletePanel(String guildId) {
        try {
            botRestClient.delete()
                    .uri("/internal/guilds/{guildId}/tickets/panel", guildId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("Failed to delete ticket panel for guild {}: {}", guildId, e.getMessage());
        }
    }

    public void deleteAllTickets(String guildId) {
        try {
            botRestClient.delete()
                    .uri("/internal/guilds/{guildId}/tickets", guildId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("Failed to delete all tickets for guild {}: {}", guildId, e.getMessage());
            throw new RuntimeException("Could not delete ticket channels", e);
        }
    }
}
