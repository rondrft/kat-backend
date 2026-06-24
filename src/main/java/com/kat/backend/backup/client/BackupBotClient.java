package com.kat.backend.backup.client;

import com.kat.backend.backup.dto.BackupDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class BackupBotClient {

    private final RestClient restClient;

    public BackupBotClient(@Qualifier("botRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<BackupDto> listBackups(String guildId) {
        try {
            List<Map<String, Object>> raw = restClient.get()
                    .uri("/internal/guilds/{guildId}/backups", guildId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            if (raw == null) return List.of();
            return raw.stream().map(this::toDto).toList();
        } catch (Exception e) {
            log.error("Failed to list backups for guild {}: {}", guildId, e.getMessage());
            return List.of();
        }
    }

    public BackupDto createBackup(String guildId, String name) {
        Map<String, Object> body = restClient.post()
                .uri("/internal/guilds/{guildId}/backups", guildId)
                .body(Map.of("name", name))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return toDto(body);
    }

    public boolean restoreBackup(String guildId, String id) {
        try {
            restClient.post()
                    .uri("/internal/guilds/{guildId}/backups/{id}/restore", guildId, id)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (HttpClientErrorException e) {
            log.error("Restore failed for backup {} in guild {}: {}", id, guildId, e.getMessage());
            return false;
        }
    }

    public void deleteBackup(String guildId, String id) {
        restClient.delete()
                .uri("/internal/guilds/{guildId}/backups/{id}", guildId, id)
                .retrieve()
                .toBodilessEntity();
    }

    private BackupDto toDto(Map<String, Object> body) {
        if (body == null) return null;
        return new BackupDto(
                (String) body.get("id"),
                (String) body.get("guildId"),
                (String) body.get("name"),
                String.valueOf(body.get("createdAt")),
                body.get("dataSize") instanceof Number n ? n.intValue() : 0
        );
    }
}
