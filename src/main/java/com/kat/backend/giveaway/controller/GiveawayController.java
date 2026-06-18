package com.kat.backend.giveaway.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.giveaway.dto.CreateGiveawayRequest;
import com.kat.backend.giveaway.dto.GiveawayParticipantDto;
import com.kat.backend.giveaway.dto.GiveawayResponse;
import com.kat.backend.giveaway.dto.RollGiveawayResponse;
import com.kat.backend.guild.service.BotGuildService;
import com.kat.backend.security.GuildAdmin;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/guilds/{guildId}/giveaways")
@RequiredArgsConstructor
public class GiveawayController {

    private final BotGuildService botGuildService;

    @PostMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<GiveawayResponse>> createGiveaway(
            @PathVariable String guildId,
            @Valid @RequestBody CreateGiveawayRequest request) {

        GiveawayResponse response = botGuildService.createGiveaway(guildId, request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/{giveawayId}")
    @GuildAdmin
    public ResponseEntity<ApiResponse<GiveawayResponse>> getGiveaway(
            @PathVariable String guildId,
            @PathVariable String giveawayId) {

        GiveawayResponse response = botGuildService.getGiveaway(guildId, giveawayId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/{giveawayId}/participants")
    @GuildAdmin
    public ResponseEntity<ApiResponse<List<GiveawayParticipantDto>>> getParticipants(
            @PathVariable String guildId,
            @PathVariable String giveawayId,
            @PageableDefault(size = 50) Pageable pageable) {

        return ResponseEntity.ok(ApiResponse.ok(botGuildService.getGiveawayParticipants(guildId, giveawayId, pageable).getContent()));
    }

    @PostMapping("/{giveawayId}/roll")
    @GuildAdmin
    public ResponseEntity<ApiResponse<RollGiveawayResponse>> rollGiveaway(
            @PathVariable String guildId,
            @PathVariable String giveawayId) {

        RollGiveawayResponse response = botGuildService.rollGiveaway(guildId, giveawayId);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
