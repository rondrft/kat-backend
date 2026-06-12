package com.kat.backend.guild.service;

import com.kat.backend.guild.dto.ActionsConfigRequest;
import com.kat.backend.leveling.dto.LevelingConfigRequest;
import com.kat.backend.guild.dto.RoleDto;
import com.kat.backend.guild.dto.SyncReactionPanelRequest;
import com.kat.backend.guild.dto.TextChannelDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BotGuildService {

    @Qualifier("botRestClient")
    private final RestClient botRestClient;

    public List<Map<String, String>> getCategories(String guildId) {
        return botRestClient.get()
                .uri("/internal/guilds/{guildId}/categories", guildId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Map<String, String>>>() {});
    }

    public Map<String, String> provisionVoice(String guildId, Map<String, Object> config) {
        return botRestClient.post()
                .uri("/internal/guilds/{guildId}/voice/provision", guildId)
                .body(config)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, String>>() {});
    }

    public List<RoleDto> getRoles(String guildId) {
        return botRestClient.get()
                .uri("/internal/guilds/{guildId}/roles", guildId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<RoleDto>>() {});
    }

    public List<TextChannelDto> getTextChannels(String guildId) {
        return botRestClient.get()
                .uri("/internal/guilds/{guildId}/channels/text", guildId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<TextChannelDto>>() {});
    }

    public Map<String, Object> getActionsConfig(String guildId) {
        return botRestClient.get()
                .uri("/internal/guilds/{guildId}/actions", guildId)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public void saveActionsConfig(String guildId, ActionsConfigRequest request) {
        botRestClient.put()
                .uri("/internal/guilds/{guildId}/actions", guildId)
                .body(Map.of("enabled", request.isEnabled()))
                .retrieve()
                .toBodilessEntity();
    }

    public String syncReactionPanel(String guildId, SyncReactionPanelRequest request) {
        Map<String, String> response = botRestClient.post()
                .uri("/internal/guilds/{guildId}/autoroles/sync", guildId)
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, String>>() {});

        return response != null ? response.get("messageId") : null;
    }

    public Map<String, Object> getLevelingConfig(String guildId) {
        return botRestClient.get()
                .uri("/internal/guilds/{guildId}/leveling", guildId)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public void saveLevelingConfig(String guildId, LevelingConfigRequest request) {
        botRestClient.put()
                .uri("/internal/guilds/{guildId}/leveling", guildId)
                .body(Map.of("enabled", request.isEnabled()))
                .retrieve()
                .toBodilessEntity();
    }
}