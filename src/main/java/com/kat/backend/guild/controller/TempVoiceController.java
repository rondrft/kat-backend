package com.kat.backend.guild.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.guild.dto.TempVoiceConfigRequest;
import com.kat.backend.guild.dto.TempVoiceConfigResponse;
import com.kat.backend.guild.service.TempVoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guilds/{guildId}/voice/temp")
@RequiredArgsConstructor
public class TempVoiceController {

    private final TempVoiceService tempVoiceService;

    @GetMapping
    public ResponseEntity<ApiResponse<TempVoiceConfigResponse>> getConfig(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId) {

        return ResponseEntity.ok(ApiResponse.ok(tempVoiceService.getConfig(guildId)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<TempVoiceConfigResponse>> saveConfig(
            @PathVariable String guildId,
            @RequestBody TempVoiceConfigRequest request,
            @AuthenticationPrincipal String discordId) {

        return ResponseEntity.ok(ApiResponse.ok(tempVoiceService.saveConfig(guildId, request)));
    }
}