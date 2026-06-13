package com.kat.backend.welcome.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.security.GuildAdmin;
import com.kat.backend.welcome.dto.WelcomeConfigDto;
import com.kat.backend.welcome.service.WelcomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/guilds/{guildId}/welcomes")
@RequiredArgsConstructor
public class WelcomeController {

    private final WelcomeService welcomeService;

    @GetMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<WelcomeConfigDto>> getConfig(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId) {

        WelcomeConfigDto config = welcomeService.getConfig(guildId);
        return ResponseEntity.ok(ApiResponse.ok(config));
    }

    @PutMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<WelcomeConfigDto>> saveConfig(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId,
            @Valid @RequestBody WelcomeConfigDto dto) {

        WelcomeConfigDto saved = welcomeService.saveConfig(guildId, dto);
        return ResponseEntity.ok(ApiResponse.ok(saved));
    }

    @PostMapping(value = "/background", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @GuildAdmin
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadBackground(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId,
            @RequestParam("file") MultipartFile file) {

        try {
            String url = welcomeService.saveBackground(guildId, file);
            return ResponseEntity.ok(ApiResponse.ok(Map.of("url", url)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to save file"));
        }
    }
}