package com.kat.backend.guild.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.guild.dto.GuildSettingsRequest;
import com.kat.backend.guild.dto.GuildSettingsResponse;
import com.kat.backend.guild.entity.GuildConfig;
import com.kat.backend.guild.service.GuildConfigService;
import com.kat.backend.security.GuildAdmin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guilds/{guildId}/settings")
@RequiredArgsConstructor
public class GuildSettingsController {

    private final GuildConfigService guildConfigService;

    @GetMapping
    public ResponseEntity<ApiResponse<GuildSettingsResponse>> getSettings(
            @PathVariable String guildId) {

        GuildConfig config = guildConfigService.getConfig(guildId);
        GuildSettingsResponse response = GuildSettingsResponse.builder()
                .guildId(config.getGuildId())
                .prefix(config.getPrefix())
                .locale(config.getLocale())
                .build();

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PatchMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<GuildSettingsResponse>> updateSettings(
            @PathVariable String guildId,
            @Valid @RequestBody GuildSettingsRequest request) {

        GuildConfig config = guildConfigService.updateConfig(
                guildId, request.getPrefix(), request.getLocale());

        GuildSettingsResponse response = GuildSettingsResponse.builder()
                .guildId(config.getGuildId())
                .prefix(config.getPrefix())
                .locale(config.getLocale())
                .build();

        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
