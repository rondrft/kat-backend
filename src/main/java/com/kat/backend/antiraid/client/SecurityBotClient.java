package com.kat.backend.antiraid.client;

import com.kat.backend.antiraid.dto.AntiRaidConfigDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Component
public class SecurityBotClient {

    private final RestClient restClient;

    public SecurityBotClient(@Qualifier("botRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public AntiRaidConfigDto getConfig(String guildId) {
        try {
            Map<String, Object> raw = restClient.get()
                    .uri("/internal/guilds/{guildId}/security", guildId)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            return toDto(raw);
        } catch (Exception e) {
            log.error("Failed to get anti-raid config for guild {}: {}", guildId, e.getMessage());
            return null;
        }
    }

    public AntiRaidConfigDto saveConfig(String guildId, AntiRaidConfigDto config) {
        try {
            Map<String, Object> raw = restClient.put()
                    .uri("/internal/guilds/{guildId}/security", guildId)
                    .body(config)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            return toDto(raw);
        } catch (Exception e) {
            log.error("Failed to save anti-raid config for guild {}: {}", guildId, e.getMessage());
            return null;
        }
    }

    private AntiRaidConfigDto toDto(Map<String, Object> body) {
        if (body == null) return null;
        return new AntiRaidConfigDto(
                (String) body.get("guildId"),
                Boolean.TRUE.equals(body.get("antiRaidEnabled")),
                toInt(body.get("joinThreshold"), 10),
                toInt(body.get("joinWindowSeconds"), 10),
                strOrDefault(body.get("joinAction"), "LOCK"),
                (String) body.get("alertChannelId"),
                toInt(body.get("minAccountAgeDays"), 0),
                Boolean.TRUE.equals(body.get("requireAvatar")),
                strOrDefault(body.get("accountAction"), "KICK"),
                Boolean.TRUE.equals(body.get("massBanEnabled")),
                Boolean.TRUE.equals(body.get("massKickEnabled")),
                Boolean.TRUE.equals(body.get("massDeleteEnabled")),
                Boolean.TRUE.equals(body.get("massWebhookEnabled")),
                toInt(body.get("massActionThreshold"), 5),
                toInt(body.get("massActionWindowSeconds"), 30),
                strOrDefault(body.get("massAction"), "ALERT")
        );
    }

    private int toInt(Object val, int def) {
        return val instanceof Number n ? n.intValue() : def;
    }

    private String strOrDefault(Object val, String def) {
        return val instanceof String s && !s.isBlank() ? s : def;
    }
}
