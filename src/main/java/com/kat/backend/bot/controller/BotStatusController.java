package com.kat.backend.bot.controller;

import com.kat.backend.bot.client.BotStatusClient;
import com.kat.backend.bot.dto.BotStatusDto;
import com.kat.backend.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/bot")
@RequiredArgsConstructor
public class BotStatusController {

    private final BotStatusClient botStatusClient;

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<BotStatusDto>> getStatus() {
        BotStatusDto status = botStatusClient.getStatus();
        if (status == null) {
            log.warn("Bot status requested but bot is unavailable");
            return ResponseEntity.status(503).body(ApiResponse.error("Bot no disponible"));
        }
        log.debug("Bot status requested: {}/{} connected, {} guilds",
                status.getConnectedShards(), status.getTotalShards(), status.getTotalGuilds());
        return ResponseEntity.ok(ApiResponse.ok(status));
    }
}
