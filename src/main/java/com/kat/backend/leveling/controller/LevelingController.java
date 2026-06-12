package com.kat.backend.leveling.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.guild.service.BotGuildService;
import com.kat.backend.leveling.dto.LevelingConfigRequest;
import com.kat.backend.leveling.dto.LevelingConfigResponse;
import com.kat.backend.security.GuildAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/guilds/{guildId}/leveling")
@RequiredArgsConstructor
public class LevelingController {

    private final BotGuildService botGuildService;

    @GetMapping
    public ResponseEntity<ApiResponse<LevelingConfigResponse>> getConfig(
            @PathVariable String guildId) {

        Map<String, Object> botResponse = botGuildService.getLevelingConfig(guildId);
        LevelingConfigResponse response = LevelingConfigResponse.builder()
                .guildId((String) botResponse.get("guildId"))
                .enabled((boolean) botResponse.get("enabled"))
                .build();

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PutMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<Void>> saveConfig(
            @PathVariable String guildId,
            @RequestBody LevelingConfigRequest request,
            @AuthenticationPrincipal String discordId) {

        botGuildService.saveLevelingConfig(guildId, request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
