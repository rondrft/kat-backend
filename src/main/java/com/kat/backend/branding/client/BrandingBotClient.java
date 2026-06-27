package com.kat.backend.branding.client;

import com.kat.backend.branding.dto.GuildBrandingDto;
import com.kat.backend.branding.dto.GuildBrandingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

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
            return new GuildBrandingDto(guildId, null, null);
        }
    }

    public GuildBrandingDto updateBranding(String guildId, GuildBrandingRequest request) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("botName", request.botName());
            payload.put("avatarUrl", request.avatarUrl());

            Map<String, Object> body = restClient.put()
                    .uri("/internal/guilds/{guildId}/branding", guildId)
                    .body(payload)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            return toDto(guildId, body);
        } catch (HttpClientErrorException e) {
            log.error("Failed to update branding for guild {}: {} - {}", guildId, e.getStatusCode(), e.getResponseBodyAsString());
            throw new ResponseStatusException(e.getStatusCode(), e.getResponseBodyAsString());
        } catch (RestClientException e) {
            log.error("Failed to update branding for guild {}: {}", guildId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Branding service unavailable");
        }
    }

    private GuildBrandingDto toDto(String guildId, Map<String, Object> body) {
        if (body == null) return new GuildBrandingDto(guildId, null, null);
        return new GuildBrandingDto(
                guildId,
                nullIfBlank((String) body.get("botName")),
                nullIfBlank((String) body.get("avatarUrl"))
        );
    }

    private String nullIfBlank(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
