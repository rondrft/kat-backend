package com.kat.backend.moderation.client;

import com.kat.backend.moderation.dto.ModPermissionDto;
import com.kat.backend.moderation.dto.PurgeConfigDto;
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

    public void savePermissions(String guildId, ModPermissionDto dto) {
        try {
            restClient.put()
                    .uri("/internal/guilds/{guildId}/moderation/permissions", guildId)
                    .body(dto)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Failed to save mod permissions for guild {}: {}", guildId, e.getMessage());
        }
    }

    public void savePurgeConfig(String guildId, PurgeConfigDto dto) {
        try {
            restClient.put()
                    .uri("/internal/guilds/{guildId}/moderation/purge", guildId)
                    .body(dto)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Failed to save purge config for guild {}: {}", guildId, e.getMessage());
        }
    }

    public ModPermissionDto getPermissions(String guildId) {
        try {
            return restClient.get()
                    .uri("/internal/guilds/{guildId}/moderation/permissions", guildId)
                    .retrieve()
                    .body(ModPermissionDto.class);
        } catch (Exception e) {
            log.error("Failed to fetch mod permissions for guild {}: {}", guildId, e.getMessage());
            return new ModPermissionDto();
        }
    }

    public PurgeConfigDto getPurgeConfig(String guildId) {
        try {
            return restClient.get()
                    .uri("/internal/guilds/{guildId}/moderation/purge", guildId)
                    .retrieve()
                    .body(PurgeConfigDto.class);
        } catch (Exception e) {
            log.error("Failed to fetch purge config for guild {}: {}", guildId, e.getMessage());
            return new PurgeConfigDto();
        }
    }

}