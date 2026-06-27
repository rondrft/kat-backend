package com.kat.backend.branding.controller;

import com.kat.backend.branding.client.BrandingBotClient;
import com.kat.backend.branding.dto.GuildBrandingDto;
import com.kat.backend.branding.dto.GuildBrandingRequest;
import com.kat.backend.common.ApiResponse;
import com.kat.backend.security.GuildAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guilds/{guildId}/branding")
@RequiredArgsConstructor
public class BrandingController {

    private static final int MAX_NAME_LENGTH = 100;
    private static final String URL_PATTERN = "^https?://.+";

    private final BrandingBotClient brandingBotClient;

    @GetMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<GuildBrandingDto>> getBranding(@PathVariable String guildId) {
        return ResponseEntity.ok(ApiResponse.ok(brandingBotClient.getBranding(guildId)));
    }

    @PutMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<GuildBrandingDto>> updateBranding(
            @PathVariable String guildId,
            @RequestBody GuildBrandingRequest request) {

        if (request.botName() != null && request.botName().length() > MAX_NAME_LENGTH) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Bot name must be 100 characters or fewer"));
        }
        if (request.avatarUrl() != null && !request.avatarUrl().matches(URL_PATTERN)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Avatar URL must start with http:// or https://"));
        }

        return ResponseEntity.ok(ApiResponse.ok(brandingBotClient.updateBranding(guildId, request)));
    }
}
