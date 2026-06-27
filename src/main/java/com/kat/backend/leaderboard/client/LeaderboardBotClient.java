package com.kat.backend.leaderboard.client;

import com.kat.backend.leaderboard.dto.LeaderboardSettingsDto;
import com.kat.backend.leaderboard.dto.ServerLeaderboardEntryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class LeaderboardBotClient {

    private final RestClient restClient;

    public LeaderboardBotClient(@Qualifier("botRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<ServerLeaderboardEntryDto> getLeaderboard() {
        try {
            List<ServerLeaderboardEntryDto> entries = restClient.get()
                    .uri("/internal/leaderboard/servers")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            return entries != null ? entries : List.of();
        } catch (Exception e) {
            log.error("Failed to fetch server leaderboard: {}", e.getMessage());
            return List.of();
        }
    }

    public LeaderboardSettingsDto getSettings(String guildId) {
        try {
            Map<String, Object> body = restClient.get()
                    .uri("/internal/leaderboard/servers/{guildId}/settings", guildId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            return toDto(guildId, body);
        } catch (HttpClientErrorException.NotFound e) {
            return new LeaderboardSettingsDto(guildId, false);
        } catch (Exception e) {
            log.error("Failed to fetch leaderboard settings for guild {}: {}", guildId, e.getMessage());
            return new LeaderboardSettingsDto(guildId, false);
        }
    }

    public LeaderboardSettingsDto updateSettings(String guildId, boolean showOnLeaderboard) {
        Map<String, Object> body = restClient.put()
                .uri("/internal/leaderboard/servers/{guildId}/settings", guildId)
                .body(Map.of("showOnLeaderboard", showOnLeaderboard))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return toDto(guildId, body);
    }

    private LeaderboardSettingsDto toDto(String guildId, Map<String, Object> body) {
        if (body == null) return new LeaderboardSettingsDto(guildId, false);
        boolean show = Boolean.TRUE.equals(body.get("showOnLeaderboard"));
        return new LeaderboardSettingsDto(guildId, show);
    }
}
