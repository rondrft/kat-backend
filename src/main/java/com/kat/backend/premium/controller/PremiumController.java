package com.kat.backend.premium.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.premium.dto.PremiumStatusDto;
import com.kat.backend.premium.service.PremiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guilds/{guildId}/premium")
@RequiredArgsConstructor
public class PremiumController {

    private final PremiumService premiumService;

    @GetMapping
    public ResponseEntity<ApiResponse<PremiumStatusDto>> getStatus(
            @PathVariable String guildId) {
        return ResponseEntity.ok(
                ApiResponse.ok(new PremiumStatusDto(premiumService.isPremium(guildId)))
        );
    }
}