package com.kat.backend.config;

import com.kat.backend.security.RateLimitInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class RateLimitConfig implements WebMvcConfigurer {

    private final RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns(
                        "/auth/discord/callback",
                        "/guilds/*/boosters/**",
                        "/guilds/*/welcomes/**",
                        "/guilds/*/autoroles/**",
                        "/guilds/*/voice/**",
                        "/guilds/*/moderation/**",
                        "/guilds/*/leveling/**",
                        "/guilds/*/logging/**",
                        "/guilds/*/actions/**",
                        "/guilds/*/giveaways/**",
                        "/guilds/*/works/**",
                        "/guilds/*/messages/**",
                        "/guilds/*/premium/**",
                        "/guilds/*/audit-logs/**",
                        "/guilds/*/ranking/**",
                        "/guilds/*/members/**",
                        "/guilds/*/channels/**",
                        "/guilds/*/roles/**",
                        "/guilds/*/stats",
                        "/owner/**"
                );
    }
}