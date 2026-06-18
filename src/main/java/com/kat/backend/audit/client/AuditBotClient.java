package com.kat.backend.audit.client;

import com.kat.backend.audit.dto.AuditLogDto;
import com.kat.backend.audit.dto.RankingEntryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    public Page<AuditLogDto> getAuditLogs(String guildId, Pageable pageable) {
        try {
            int size = Math.min(pageable.getPageSize(), 100);
            List<AuditLogDto> logs = restClient.get()
                    .uri("/internal/guilds/{guildId}/audit-logs?limit={limit}", guildId, size)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            List<AuditLogDto> list = logs != null ? logs : List.of();
            return new PageImpl<>(list, pageable, list.size());
        } catch (Exception e) {
            log.error("Failed to fetch audit logs for guild {}: {}", guildId, e.getMessage());
            return Page.empty();
        }
    }

    public Page<RankingEntryDto> getRanking(String guildId, Pageable pageable) {
        try {
            int size = Math.min(pageable.getPageSize(), 100);
            List<RankingEntryDto> ranking = restClient.get()
                    .uri("/internal/guilds/{guildId}/ranking?limit={limit}", guildId, size)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            List<RankingEntryDto> list = ranking != null ? ranking : List.of();
            return new PageImpl<>(list, pageable, list.size());
        } catch (Exception e) {
            log.error("Failed to fetch ranking for guild {}: {}", guildId, e.getMessage());
            return Page.empty();
        }
    }
}