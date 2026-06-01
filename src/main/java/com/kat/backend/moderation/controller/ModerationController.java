package com.kat.backend.moderation.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.moderation.dto.ModerationConfigDto;
import com.kat.backend.moderation.service.ModerationService;
import com.kat.backend.security.GuildAdmin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guilds/{guildId}/moderation")
@RequiredArgsConstructor
public class ModerationController {

    private final ModerationService moderationService;

    @GetMapping
    public ResponseEntity<ApiResponse<ModerationConfigDto>> getConfig(@PathVariable String guildId) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.getConfig(guildId)));
    }

    @PutMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<ModerationConfigDto>> saveConfig(
            @PathVariable String guildId,
            @Valid @RequestBody ModerationConfigDto dto
    ) {
        return ResponseEntity.ok(ApiResponse.ok(moderationService.saveConfig(guildId, dto)));
    }
}