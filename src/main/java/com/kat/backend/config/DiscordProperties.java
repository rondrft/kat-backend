package com.kat.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "discord")
public class DiscordProperties {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    /** Bot token — required for GUILD_MEMBER_JOIN events (dashboard chart + avatars). */
    private String botToken;
}