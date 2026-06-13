package com.kat.backend.guild.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.guild.dto.AutoRolesConfigRequest;
import com.kat.backend.guild.dto.AutoRolesConfigResponse;
import com.kat.backend.guild.service.AutoRolesService;
import com.kat.backend.security.GuildAdmin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guilds/{guildId}/autoroles")
@RequiredArgsConstructor
public class AutoRolesController {

    private final AutoRolesService autoRolesService;

    @GetMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<AutoRolesConfigResponse>> getConfig(
            @PathVariable String guildId) {

        return ResponseEntity.ok(
                ApiResponse.ok(autoRolesService.getConfig(guildId))
        );
    }

    @PutMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<AutoRolesConfigResponse>> saveConfig(
            @PathVariable String guildId,
            @Valid @RequestBody AutoRolesConfigRequest request,
            @AuthenticationPrincipal String discordId) {

        return ResponseEntity.ok(
                ApiResponse.ok(autoRolesService.saveConfig(guildId, request))
        );
    }
}