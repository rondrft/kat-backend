package com.kat.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BotClientConfig {

    @Value("${internal.bot-url}")
    private String botUrl;

    @Value("${internal.api-key}")
    private String apiKey;

    @Bean(name = "botRestClient")
    public RestClient botRestClient() {
        return RestClient.builder()
                .baseUrl(botUrl)
                .defaultHeader("X-Internal-Api-Key", apiKey)
                .build();
    }
}