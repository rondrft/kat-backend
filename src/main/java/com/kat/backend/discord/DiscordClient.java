package com.kat.backend.discord;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kat.backend.config.DiscordProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordClient {

    private static final String DISCORD_API = "https://discord.com/api/v10";
    private static final int MAX_RETRIES = 3;

    private final RestClient restClient;
    private final DiscordProperties discordProperties;
    private final ObjectMapper objectMapper;

    public DiscordTokenResponse exchangeCodeForToken(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", discordProperties.getClientId());
        body.add("client_secret", discordProperties.getClientSecret());
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", discordProperties.getRedirectUri());

        return executeWithRetry(() -> restClient.post()
                .uri(DISCORD_API + "/oauth2/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(body)
                .retrieve()
                .body(DiscordTokenResponse.class), "exchangeCodeForToken");
    }

    public DiscordUserResponse getUserInfo(String accessToken) {
        return executeWithRetry(() -> restClient.get()
                .uri(DISCORD_API + "/users/@me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(DiscordUserResponse.class), "getUserInfo");
    }

    public List<DiscordGuildResponse> getUserGuilds(String accessToken) {
        return executeWithRetry(() -> restClient.get()
                .uri(DISCORD_API + "/users/@me/guilds")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<List<DiscordGuildResponse>>() {}), "getUserGuilds");
    }

    private <T> T executeWithRetry(Supplier<T> call, String operation) {
        int attempt = 0;
        while (true) {
            try {
                return call.get();
            } catch (HttpStatusCodeException ex) {
                if (ex.getStatusCode().value() == 429 && attempt < MAX_RETRIES) {
                    long retryAfterMs = parseRetryAfter(ex);
                    log.warn("Discord API 429 on {}. Retry {}/{} after {}ms", operation, attempt + 1, MAX_RETRIES, retryAfterMs);
                    try {
                        Thread.sleep(retryAfterMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Interrupted during Discord retry", ie);
                    }
                    attempt++;
                } else {
                    throw ex;
                }
            }
        }
    }

    private long parseRetryAfter(HttpStatusCodeException ex) {
        try {
            String body = ex.getResponseBodyAsString();
            JsonNode json = objectMapper.readTree(body);
            if (json.has("retry_after")) {
                double seconds = json.get("retry_after").asDouble(1.0);
                return (long) (seconds * 1000) + 500;
            }
        } catch (Exception e) {
            log.warn("Could not parse Discord 429 retry_after", e);
        }
        return 1500;
    }
}