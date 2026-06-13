package com.kat.backend.guild.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.guild.dto.ActionsConfigRequest;
import com.kat.backend.guild.dto.ActionsConfigResponse;
import com.kat.backend.guild.service.BotGuildService;
import com.kat.backend.security.GuildAdmin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/guilds/{guildId}/actions")
@RequiredArgsConstructor
public class ActionsController {

    private final BotGuildService botGuildService;

    @GetMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<ActionsConfigResponse>> getConfig(
            @PathVariable String guildId) {

        Map<String, Object> botResponse = botGuildService.getActionsConfig(guildId);
        ActionsConfigResponse response = ActionsConfigResponse.builder()
                .guildId((String) botResponse.get("guildId"))
                .enabled((boolean) botResponse.get("enabled"))
                .build();

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PutMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<Void>> saveConfig(
            @PathVariable String guildId,
            @Valid @RequestBody ActionsConfigRequest request,
            @AuthenticationPrincipal String discordId) {

        botGuildService.saveActionsConfig(guildId, request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
