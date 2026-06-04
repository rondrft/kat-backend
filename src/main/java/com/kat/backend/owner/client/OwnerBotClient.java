package com.kat.backend.owner.client;

import com.kat.backend.owner.dto.OwnerMetricsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class OwnerBotClient {

    private final RestClient restClient;

    public OwnerBotClient(@Qualifier("botRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public OwnerMetricsDto getMetrics() {
        try {
            return restClient.get()
                    .uri("/internal/owner/metrics")
                    .retrieve()
                    .body(OwnerMetricsDto.class);
        } catch (Exception e) {
            log.error("Failed to fetch owner metrics: {}", e.getMessage());
            return new OwnerMetricsDto();
        }
    }
}