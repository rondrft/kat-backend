package com.kat.backend.guild.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.guild.dto.*;
import com.kat.backend.guild.service.BotGuildService;
import com.kat.backend.guild.service.GuildService;
import com.kat.backend.guild.service.GuildStatsService;
import com.kat.backend.security.GuildAdmin;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/guilds")
@RequiredArgsConstructor
public class GuildController {

    private final GuildService guildService;
    private final BotGuildService botGuildService;
    private final GuildStatsService  guildStatsService;

    @GetMapping("/{guildId}")
    @GuildAdmin
    public ResponseEntity<ApiResponse<Map<String, Object>>> getGuildById(
            @PathVariable String guildId) {
        return ResponseEntity.ok(ApiResponse.ok(botGuildService.getGuildInfo(guildId)));
    }

    @GetMapping
    public ResponseEntity<Page<GuildUserResponse>> getUserGuilds(
            @AuthenticationPrincipal String discordId,
            @PageableDefault(size = 100) Pageable pageable) {

        return ResponseEntity.ok(guildService.getUserGuilds(discordId, pageable));
    }

    @GetMapping("/{guildId}/members/joins/monthly")
    public ResponseEntity<ApiResponse<MonthlyJoinStatsDto>> getMonthlyJoinStats(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId) {

        MonthlyJoinStatsDto stats = guildService.getMonthlyJoinStats(guildId);
        return ResponseEntity.ok(ApiResponse.ok(stats));
    }

    @GetMapping("/{guildId}/members/recent")
    public ResponseEntity<ApiResponse<List<RecentMemberDto>>> getRecentMembers(
            @PathVariable String guildId,
            @PageableDefault(size = 50) Pageable pageable,
            @AuthenticationPrincipal String discordId) {

        return ResponseEntity.ok(ApiResponse.ok(guildService.getRecentMembers(guildId, pageable).getContent()));
    }

    @GetMapping("/{guildId}/members/stats")
    public ResponseEntity<ApiResponse<List<MemberJoinStatDto>>> getMemberJoinStats(
            @PathVariable String guildId,
            @Max(365) @RequestParam(defaultValue = "30") int days,
            @AuthenticationPrincipal String discordId) {

        List<MemberJoinStatDto> stats = guildService.getMemberJoinStats(guildId, days);
        return ResponseEntity.ok(ApiResponse.ok(stats));
    }

    @GetMapping("/{guildId}/channels/categories")
    @GuildAdmin
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> getCategories(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId,
            @PageableDefault(size = 50) Pageable pageable) {

        return ResponseEntity.ok(ApiResponse.ok(botGuildService.getCategories(guildId, pageable).getContent()));
    }

    @GetMapping("/{guildId}/roles")
    @GuildAdmin
    public ResponseEntity<ApiResponse<List<RoleDto>>> getRoles(
            @PathVariable String guildId,
            @PageableDefault(size = 50) Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponse.ok(botGuildService.getRoles(guildId, pageable).getContent())
        );
    }

    @GetMapping("/{guildId}/channels/text")
    @GuildAdmin
    public ResponseEntity<ApiResponse<List<TextChannelDto>>> getTextChannels(
            @PathVariable String guildId,
            @PageableDefault(size = 50) Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponse.ok(botGuildService.getTextChannels(guildId, pageable).getContent())
        );
    }

    @GetMapping("/{guildId}/stats")
    public ResponseEntity<ApiResponse<GuildStatsDto>> getGuildStats(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId) {

        return ResponseEntity.ok(ApiResponse.ok(guildStatsService.getStats(guildId)));
    }
}