package com.kat.backend.message.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.guild.service.BotGuildService;
import com.kat.backend.message.dto.SendMessageRequest;
import com.kat.backend.message.dto.SendMessageResponse;
import com.kat.backend.security.GuildAdmin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guilds/{guildId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final BotGuildService botGuildService;

    @PostMapping("/send")
    @GuildAdmin
    public ResponseEntity<ApiResponse<SendMessageResponse>> sendMessage(
            @PathVariable String guildId,
            @Valid @RequestBody SendMessageRequest request) {

        SendMessageResponse response = botGuildService.sendMessage(guildId, request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
