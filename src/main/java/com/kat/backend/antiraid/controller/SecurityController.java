package com.kat.backend.antiraid.controller;

import com.kat.backend.antiraid.client.SecurityBotClient;
import com.kat.backend.antiraid.dto.AntiRaidConfigDto;
import com.kat.backend.common.ApiResponse;
import com.kat.backend.security.GuildAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guilds/{guildId}/security")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityBotClient securityBotClient;

    @GetMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<AntiRaidConfigDto>> getConfig(@PathVariable String guildId) {
        AntiRaidConfigDto config = securityBotClient.getConfig(guildId);
        if (config == null) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to fetch security config"));
        }
        return ResponseEntity.ok(ApiResponse.ok(config));
    }

    @PutMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<AntiRaidConfigDto>> saveConfig(
            @PathVariable String guildId,
            @RequestBody AntiRaidConfigDto config) {
        AntiRaidConfigDto saved = securityBotClient.saveConfig(guildId, config);
        if (saved == null) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to save security config"));
        }
        return ResponseEntity.ok(ApiResponse.ok(saved));
    }
}
