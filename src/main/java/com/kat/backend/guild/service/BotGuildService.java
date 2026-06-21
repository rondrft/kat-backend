package com.kat.backend.guild.service;

import com.kat.backend.guild.dto.ActionsConfigRequest;
import com.kat.backend.leveling.dto.LevelingConfigRequest;
import com.kat.backend.works.dto.WorkConfigRequest;
import com.kat.backend.guild.dto.RoleDto;
import com.kat.backend.guild.dto.SyncReactionPanelRequest;
import com.kat.backend.guild.dto.TextChannelDto;
import com.kat.backend.giveaway.dto.CreateGiveawayRequest;
import com.kat.backend.giveaway.dto.GiveawayParticipantDto;
import com.kat.backend.giveaway.dto.GiveawayResponse;
import com.kat.backend.giveaway.dto.RollGiveawayResponse;
import com.kat.backend.message.dto.SendMessageRequest;
import com.kat.backend.message.dto.SendMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BotGuildService {

    @Qualifier("botRestClient")
    private final RestClient botRestClient;

    public Map<String, Object> getGuildInfo(String guildId) {
        return botRestClient.get()
                .uri("/internal/guilds/{guildId}", guildId)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Page<Map<String, String>> getCategories(String guildId, Pageable pageable) {
        List<Map<String, String>> categories = botRestClient.get()
                .uri("/internal/guilds/{guildId}/categories", guildId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Map<String, String>>>() {});
        return paginateList(categories, pageable);
    }

    public Map<String, String> provisionVoice(String guildId, Map<String, Object> config) {
        return botRestClient.post()
                .uri("/internal/guilds/{guildId}/voice/provision", guildId)
                .body(config)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, String>>() {});
    }

    public void deleteAllTempVoiceChannels(String guildId) {
        botRestClient.delete()
                .uri("/internal/guilds/{guildId}/voice/temp/channels", guildId)
                .retrieve()
                .toBodilessEntity();
    }

    public Page<RoleDto> getRoles(String guildId, Pageable pageable) {
        List<RoleDto> roles = botRestClient.get()
                .uri("/internal/guilds/{guildId}/roles", guildId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<RoleDto>>() {});
        return paginateList(roles, pageable);
    }

    public Page<TextChannelDto> getTextChannels(String guildId, Pageable pageable) {
        List<TextChannelDto> channels = botRestClient.get()
                .uri("/internal/guilds/{guildId}/channels/text", guildId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<TextChannelDto>>() {});
        return paginateList(channels, pageable);
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

    public SendMessageResponse sendMessage(String guildId, SendMessageRequest request) {
        return botRestClient.post()
                .uri("/internal/guilds/{guildId}/messages/send", guildId)
                .body(request)
                .retrieve()
                .body(SendMessageResponse.class);
    }

    public GiveawayResponse createGiveaway(String guildId, CreateGiveawayRequest request) {
        return botRestClient.post()
                .uri("/internal/guilds/{guildId}/giveaways", guildId)
                .body(request)
                .retrieve()
                .body(GiveawayResponse.class);
    }

    public GiveawayResponse getGiveaway(String guildId, String giveawayId) {
        return botRestClient.get()
                .uri("/internal/guilds/{guildId}/giveaways/{giveawayId}", guildId, giveawayId)
                .retrieve()
                .body(GiveawayResponse.class);
    }

    public Page<GiveawayParticipantDto> getGiveawayParticipants(String guildId, String giveawayId, Pageable pageable) {
        List<GiveawayParticipantDto> participants = botRestClient.get()
                .uri("/internal/guilds/{guildId}/giveaways/{giveawayId}/participants", guildId, giveawayId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<GiveawayParticipantDto>>() {});
        return paginateList(participants, pageable);
    }

    public Map<String, Object> getWorkConfig(String guildId) {
        return botRestClient.get()
                .uri("/internal/guilds/{guildId}/works", guildId)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public Map<String, Object> saveWorkConfig(String guildId, WorkConfigRequest request) {
        var body = new java.util.HashMap<String, Object>();
        body.put("enabled", request.isEnabled());
        body.put("allowedChannelIds", request.getAllowedChannelIds() != null ? request.getAllowedChannelIds() : List.of());
        return botRestClient.put()
                .uri("/internal/guilds/{guildId}/works", guildId)
                .body(body)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public RollGiveawayResponse rollGiveaway(String guildId, String giveawayId) {
        return botRestClient.post()
                .uri("/internal/guilds/{guildId}/giveaways/{giveawayId}/roll", guildId, giveawayId)
                .retrieve()
                .body(RollGiveawayResponse.class);
    }

    public List<RoleDto> getSecurityScanRoles(String guildId) {
        return botRestClient.get()
                .uri("/internal/guilds/{guildId}/security-scan/roles", guildId)
                .retrieve()
                .body(new ParameterizedTypeReference<List<RoleDto>>() {});
    }

    private <T> Page<T> paginateList(List<T> list, Pageable pageable) {
        if (list == null || list.isEmpty()) {
            return Page.empty();
        }
        if (pageable.isUnpaged()) {
            return new PageImpl<>(list, pageable, list.size());
        }
        int start = Math.min((int) pageable.getOffset(), list.size());
        int end = Math.min(start + pageable.getPageSize(), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }
}