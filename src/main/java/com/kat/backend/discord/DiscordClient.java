package com.kat.backend.discord;

import com.kat.backend.config.DiscordProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import com.kat.backend.discord.DiscordGuildResponse;
import org.springframework.core.ParameterizedTypeReference;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DiscordClient {

    private static final String DISCORD_API = "https://discord.com/api/v10";

    private final RestClient restClient;
    private final DiscordProperties discordProperties;

    public DiscordTokenResponse exchangeCodeForToken(String code) {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", discordProperties.getClientId());
        body.add("client_secret", discordProperties.getClientSecret());
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", discordProperties.getRedirectUri());

        return restClient.post()
                .uri(DISCORD_API + "/oauth2/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(body)
                .retrieve()
                .body(DiscordTokenResponse.class);
    }

    public DiscordUserResponse getUserInfo(String accessToken) {

        return restClient.get()
                .uri(DISCORD_API + "/users/@me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(DiscordUserResponse.class);
    }

    public List<DiscordGuildResponse> getUserGuilds(String accessToken) {
        return restClient.get()
                .uri("https://discord.com/api/v10/users/@me/guilds")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<List<DiscordGuildResponse>>() {});
    }
}