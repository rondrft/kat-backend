package com.kat.backend.logging.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.logging.dto.LoggingConfigDto;
import com.kat.backend.logging.service.LoggingService;
import com.kat.backend.security.GuildAdmin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guilds/{guildId}/logging")
@RequiredArgsConstructor
public class LoggingController {

    private final LoggingService loggingService;

    @GetMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<LoggingConfigDto>> getConfig(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId) {
        return loggingService.getConfig(guildId)
                .map(dto -> ResponseEntity.ok(ApiResponse.ok(dto)))
                .orElseGet(() -> ResponseEntity.status(404)
                        .body(ApiResponse.error("No logging config found")));
    }

    @PutMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<LoggingConfigDto>> saveConfig(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId,
            @Valid @RequestBody LoggingConfigDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(loggingService.saveConfig(guildId, dto)));
    }
}