package com.kat.backend.audit.client;

import com.kat.backend.audit.dto.AuditLogDto;
import com.kat.backend.audit.dto.RankingEntryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Component
public class AuditBotClient {

    private final RestClient restClient;

    public AuditBotClient(@Qualifier("botRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<AuditLogDto> getAuditLogs(String guildId, int limit) {
        try {
            return restClient.get()
                    .uri("/internal/guilds/{guildId}/audit-logs?limit={limit}", guildId, limit)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (Exception e) {
            log.error("Failed to fetch audit logs for guild {}: {}", guildId, e.getMessage());
            return List.of();
        }
    }

    public List<RankingEntryDto> getRanking(String guildId, int limit) {
        try {
            return restClient.get()
                    .uri("/internal/guilds/{guildId}/ranking?limit={limit}", guildId, limit)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (Exception e) {
            log.error("Failed to fetch ranking for guild {}: {}", guildId, e.getMessage());
            return List.of();
        }
    }
}