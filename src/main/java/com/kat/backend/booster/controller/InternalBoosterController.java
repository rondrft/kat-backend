package com.kat.backend.booster.controller;

import com.kat.backend.booster.dto.BoosterConfigResponse;
import com.kat.backend.booster.service.BoosterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/guilds/{guildId}/boosters")
@RequiredArgsConstructor
public class InternalBoosterController {

    private final BoosterService boosterService;

    @GetMapping("/config")
    public ResponseEntity<BoosterConfigResponse> getConfig(@PathVariable String guildId) {
        return ResponseEntity.ok(boosterService.getSettings(guildId));
    }
}