package com.kat.backend.owner.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.owner.client.OwnerBotClient;
import com.kat.backend.owner.dto.OwnerMetricsDto;
import com.kat.backend.security.OwnerGuard;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerBotClient ownerBotClient;
    private final OwnerGuard ownerGuard;

    @GetMapping("/metrics")
    public ResponseEntity<ApiResponse<OwnerMetricsDto>> getMetrics(
            @AuthenticationPrincipal String discordId) {
        if (!ownerGuard.isOwner(discordId)) {
            return ResponseEntity.status(403).body(ApiResponse.error("Forbidden"));
        }
        OwnerMetricsDto metrics = ownerBotClient.getMetrics();
        return ResponseEntity.ok(ApiResponse.ok(metrics));
    }
}