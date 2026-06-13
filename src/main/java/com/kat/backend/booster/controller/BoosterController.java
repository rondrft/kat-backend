package com.kat.backend.booster.controller;

import com.kat.backend.booster.dto.BoosterConfigRequest;
import com.kat.backend.booster.dto.BoosterConfigResponse;
import com.kat.backend.booster.dto.BoosterCustomRoleResponse;
import com.kat.backend.booster.service.BoosterService;
import com.kat.backend.common.ApiResponse;
import com.kat.backend.security.GuildAdmin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/guilds/{guildId}/boosters")
@RequiredArgsConstructor
public class BoosterController {

    private final BoosterService boosterService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<BoosterCustomRoleResponse>> getMyCustomRole(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId) {

        BoosterCustomRoleResponse response = boosterService.getMyCustomRole(guildId, discordId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BoosterCustomRoleResponse>>> getAllCustomRoles(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId,
            Pageable pageable) {

        Page<BoosterCustomRoleResponse> response = boosterService.getAllCustomRoles(guildId, pageable);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/settings")
    @GuildAdmin
    public ResponseEntity<ApiResponse<BoosterConfigResponse>> getSettings(
            @PathVariable String guildId) {

        BoosterConfigResponse response = boosterService.getSettings(guildId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PutMapping("/settings")
    @GuildAdmin
    public ResponseEntity<ApiResponse<BoosterConfigResponse>> updateSettings(
            @PathVariable String guildId,
            @Valid @RequestBody BoosterConfigRequest request) {

        BoosterConfigResponse response = boosterService.updateSettings(guildId, request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PostMapping("/sync")
    @GuildAdmin
    public ResponseEntity<ApiResponse<Map<String, Integer>>> syncBoosters(
            @PathVariable String guildId) {

        int synced = boosterService.syncBoosters(guildId);
        return ResponseEntity.ok(ApiResponse.ok(Map.of("synced", synced)));
    }
}