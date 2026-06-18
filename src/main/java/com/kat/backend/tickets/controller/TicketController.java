package com.kat.backend.tickets.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.security.GuildAdmin;
import com.kat.backend.tickets.dto.TicketConfigRequest;
import com.kat.backend.tickets.dto.TicketConfigResponse;
import com.kat.backend.tickets.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guilds/{guildId}/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<TicketConfigResponse>> getConfig(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId) {

        TicketConfigResponse config = ticketService.getConfig(guildId);
        return ResponseEntity.ok(ApiResponse.ok(config));
    }

    @PutMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<TicketConfigResponse>> saveConfig(
            @PathVariable String guildId,
            @Valid @RequestBody TicketConfigRequest request,
            @AuthenticationPrincipal String discordId) {

        TicketConfigResponse saved = ticketService.saveConfig(guildId, request);
        return ResponseEntity.ok(ApiResponse.ok(saved));
    }

    @DeleteMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<Void>> resetSystem(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId) {

        ticketService.resetSystem(guildId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
