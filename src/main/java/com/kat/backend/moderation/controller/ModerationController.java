package com.kat.backend.moderation.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.moderation.dto.*;
import com.kat.backend.moderation.service.ModerationLogService;
import com.kat.backend.moderation.service.ModerationService;
import com.kat.backend.moderation.service.SecurityScanService;
import com.kat.backend.security.GuildAdmin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/guilds/{guildId}/moderation")
@RequiredArgsConstructor
public class ModerationController {

    private final ModerationService moderationService;
    private final ModerationLogService moderationLogService;
    private final SecurityScanService securityScanService;

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

    @GetMapping("/security-scan")
    @GuildAdmin
    public ResponseEntity<ApiResponse<SecurityScanResultDto>> securityScan(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId) {
        return ResponseEntity.ok(ApiResponse.ok(securityScanService.scan(guildId)));
    }

    @GetMapping("/whitelist")
    @GuildAdmin
    public ResponseEntity<ApiResponse<List<ModerationWhitelistDto>>> getWhitelist(
            @PathVariable String guildId) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.getWhitelist(guildId)));
    }

    @PostMapping("/whitelist")
    @GuildAdmin
    public ResponseEntity<ApiResponse<ModerationWhitelistDto>> addWhitelist(
            @PathVariable String guildId,
            @RequestBody ModerationWhitelistDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.addWhitelist(guildId, dto)));
    }

    @DeleteMapping("/whitelist/{entryId}")
    @GuildAdmin
    public ResponseEntity<ApiResponse<Void>> removeWhitelist(
            @PathVariable String guildId,
            @PathVariable UUID entryId) {
        moderationService.removeWhitelist(guildId, entryId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/filters")
    @GuildAdmin
    public ResponseEntity<ApiResponse<List<ModerationFilterDto>>> getFilters(
            @PathVariable String guildId) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.getFilters(guildId)));
    }

    @PostMapping("/filters")
    @GuildAdmin
    public ResponseEntity<ApiResponse<ModerationFilterDto>> addFilter(
            @PathVariable String guildId,
            @RequestBody ModerationFilterDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.addFilter(guildId, dto)));
    }

    @PutMapping("/filters/{filterId}")
    @GuildAdmin
    public ResponseEntity<ApiResponse<ModerationFilterDto>> updateFilter(
            @PathVariable String guildId,
            @PathVariable UUID filterId,
            @RequestBody ModerationFilterDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.updateFilter(guildId, filterId, dto)));
    }

    @DeleteMapping("/filters/{filterId}")
    @GuildAdmin
    public ResponseEntity<ApiResponse<Void>> deleteFilter(
            @PathVariable String guildId,
            @PathVariable UUID filterId) {
        moderationService.deleteFilter(guildId, filterId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/auto-punishments")
    @GuildAdmin
    public ResponseEntity<ApiResponse<List<ModerationAutoPunishmentDto>>> getAutoPunishments(
            @PathVariable String guildId) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.getAutoPunishments(guildId)));
    }

    @PutMapping("/auto-punishments")
    @GuildAdmin
    public ResponseEntity<ApiResponse<List<ModerationAutoPunishmentDto>>> saveAutoPunishments(
            @PathVariable String guildId,
            @RequestBody List<ModerationAutoPunishmentDto> dtos) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.saveAutoPunishments(guildId, dtos)));
    }

    @GetMapping("/log-channel")
    @GuildAdmin
    public ResponseEntity<ApiResponse<Map<String, String>>> getLogChannel(
            @PathVariable String guildId) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.getLogChannel(guildId)));
    }

    @PutMapping("/log-channel")
    @GuildAdmin
    public ResponseEntity<ApiResponse<Void>> saveLogChannel(
            @PathVariable String guildId,
            @RequestBody Map<String, String> body) {
        moderationService.saveLogChannel(guildId, body.get("logChannelId"), body.get("premiumLogChannelId"));
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
