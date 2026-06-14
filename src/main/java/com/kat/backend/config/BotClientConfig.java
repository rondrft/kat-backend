package com.kat.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

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
                .requestFactory(requestFactory())
                .build();
    }

    private ClientHttpRequestFactory requestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(15));
        return factory;
    }
}
