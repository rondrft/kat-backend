package com.kat.backend.moderation.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.moderation.dto.ModPermissionDto;
import com.kat.backend.moderation.dto.ModerationConfigDto;
import com.kat.backend.moderation.dto.ModerationLogPageDto;
import com.kat.backend.moderation.dto.NukeConfigDto;
import com.kat.backend.moderation.dto.PurgeConfigDto;
import com.kat.backend.moderation.service.ModerationLogService;
import com.kat.backend.moderation.service.ModerationService;
import com.kat.backend.security.GuildAdmin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guilds/{guildId}/moderation")
@RequiredArgsConstructor
public class ModerationController {

    private final ModerationService moderationService;
    private final ModerationLogService moderationLogService;

    @GetMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<ModerationConfigDto>> getConfig(
            @PathVariable String guildId) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.getConfig(guildId)));
    }

    @PutMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<ModerationConfigDto>> saveConfig(
            @PathVariable String guildId,
            @Valid @RequestBody ModerationConfigDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.saveConfig(guildId, dto)));
    }

    @GetMapping("/logs")
    @GuildAdmin
    public ResponseEntity<ApiResponse<ModerationLogPageDto>> getLogs(
            @PathVariable String guildId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(moderationLogService.getLogs(guildId, page, size)));
    }

    @GetMapping("/permissions")
    @GuildAdmin
    public ResponseEntity<ApiResponse<ModPermissionDto>> getPermissions(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.getPermissions(guildId)));
    }

    @PutMapping("/permissions")
    @GuildAdmin
    public ResponseEntity<ApiResponse<ModPermissionDto>> savePermissions(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId,
            @Valid @RequestBody ModPermissionDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.savePermissions(guildId, dto)));
    }

    @GetMapping("/purge")
    @GuildAdmin
    public ResponseEntity<ApiResponse<PurgeConfigDto>> getPurgeConfig(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.getPurgeConfig(guildId)));
    }

    @PutMapping("/purge")
    @GuildAdmin
    public ResponseEntity<ApiResponse<PurgeConfigDto>> savePurgeConfig(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId,
            @Valid @RequestBody PurgeConfigDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.savePurgeConfig(guildId, dto)));
    }

    @GetMapping("/nuke")
    @GuildAdmin
    public ResponseEntity<ApiResponse<NukeConfigDto>> getNukeConfig(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.getNukeConfig(guildId)));
    }

    @PutMapping("/nuke")
    @GuildAdmin
    public ResponseEntity<ApiResponse<NukeConfigDto>> saveNukeConfig(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId,
            @Valid @RequestBody NukeConfigDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.saveNukeConfig(guildId, dto)));
    }
}