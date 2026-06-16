package com.kat.backend.works.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.guild.service.BotGuildService;
import com.kat.backend.security.GuildAdmin;
import com.kat.backend.works.dto.WorkConfigRequest;
import com.kat.backend.works.dto.WorkConfigResponse;
import jakarta.validation.Valid;
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
@RequestMapping("/guilds/{guildId}/works")
@RequiredArgsConstructor
public class WorkController {

    private final BotGuildService botGuildService;

    @GetMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<WorkConfigResponse>> getConfig(
            @PathVariable String guildId) {

        Map<String, Object> botResponse = botGuildService.getWorkConfig(guildId);
        WorkConfigResponse response = WorkConfigResponse.builder()
                .guildId((String) botResponse.get("guildId"))
                .enabled((boolean) botResponse.get("enabled"))
                .build();

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PutMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<WorkConfigResponse>> saveConfig(
            @PathVariable String guildId,
            @Valid @RequestBody WorkConfigRequest request,
            @AuthenticationPrincipal String discordId) {

        Map<String, Object> botResponse = botGuildService.saveWorkConfig(guildId, request);
        WorkConfigResponse response = WorkConfigResponse.builder()
                .guildId((String) botResponse.get("guildId"))
                .enabled((boolean) botResponse.get("enabled"))
                .build();
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
