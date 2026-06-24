package com.kat.backend.branding.client;

import com.kat.backend.branding.dto.GuildBrandingDto;
import com.kat.backend.branding.dto.GuildBrandingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class BrandingBotClient {

    private final RestClient restClient;

    public BrandingBotClient(@Qualifier("botRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public GuildBrandingDto getBranding(String guildId) {
        try {
            Map<String, Object> body = restClient.get()
                    .uri("/internal/guilds/{guildId}/branding", guildId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            return toDto(guildId, body);
        } catch (Exception e) {
            log.error("Failed to fetch branding for guild {}: {}", guildId, e.getMessage());
            return new GuildBrandingDto(guildId, null, null, null, null);
        }
    }

    public GuildBrandingDto updateBranding(String guildId, GuildBrandingRequest request) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("botName", request.botName());
        payload.put("avatarUrl", request.avatarUrl());
        payload.put("bannerUrl", request.bannerUrl());
        payload.put("description", request.description());

        Map<String, Object> body = restClient.put()
                .uri("/internal/guilds/{guildId}/branding", guildId)
                .body(payload)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return toDto(guildId, body);
    }

    private GuildBrandingDto toDto(String guildId, Map<String, Object> body) {
        if (body == null) return new GuildBrandingDto(guildId, null, null, null, null);
        return new GuildBrandingDto(
                guildId,
                nullIfBlank((String) body.get("botName")),
                nullIfBlank((String) body.get("avatarUrl")),
                nullIfBlank((String) body.get("bannerUrl")),
                nullIfBlank((String) body.get("description"))
        );
    }

    private String nullIfBlank(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
