package com.kat.backend.guild.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.guild.dto.*;
import com.kat.backend.guild.service.BotGuildService;
import com.kat.backend.guild.service.GuildService;
import com.kat.backend.guild.service.GuildStatsService;
import com.kat.backend.security.GuildAdmin;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public ResponseEntity<ApiResponse<List<GuildUserResponse>>> getUserGuilds(
            @AuthenticationPrincipal String discordId) {

        List<GuildUserResponse> guilds = guildService.getUserGuilds(discordId);
        return ResponseEntity.ok(ApiResponse.ok(guilds));
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
            @Max(100) @RequestParam(defaultValue = "12") int limit,
            @AuthenticationPrincipal String discordId) {

        List<RecentMemberDto> members = guildService.getRecentMembers(guildId, limit);
        return ResponseEntity.ok(ApiResponse.ok(members));
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
            @AuthenticationPrincipal String discordId) {

        List<Map<String, String>> categories = botGuildService.getCategories(guildId);
        return ResponseEntity.ok(ApiResponse.ok(categories));
    }

    @GetMapping("/{guildId}/roles")
    @GuildAdmin
    public ResponseEntity<ApiResponse<List<RoleDto>>> getRoles(
            @PathVariable String guildId) {

        return ResponseEntity.ok(
                ApiResponse.ok(botGuildService.getRoles(guildId))
        );
    }

    @GetMapping("/{guildId}/channels/text")
    @GuildAdmin
    public ResponseEntity<ApiResponse<List<TextChannelDto>>> getTextChannels(
            @PathVariable String guildId) {

        return ResponseEntity.ok(
                ApiResponse.ok(botGuildService.getTextChannels(guildId))
        );
    }

    @GetMapping("/{guildId}/stats")
    public ResponseEntity<ApiResponse<GuildStatsDto>> getGuildStats(
            @PathVariable String guildId,
            @AuthenticationPrincipal String discordId) {

        return ResponseEntity.ok(ApiResponse.ok(guildStatsService.getStats(guildId)));
    }
}