package com.kat.backend.bot.controller;

import com.kat.backend.bot.client.BotStatusClient;
import com.kat.backend.bot.dto.BotStatusDto;
import com.kat.backend.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bot")
@RequiredArgsConstructor
public class BotStatusController {

    private final BotStatusClient botStatusClient;

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<BotStatusDto>> getStatus() {
        BotStatusDto status = botStatusClient.getStatus();
        if (status == null) {
            return ResponseEntity.status(503).body(ApiResponse.error("Bot no disponible"));
        }
        return ResponseEntity.ok(ApiResponse.ok(status));
    }
}
