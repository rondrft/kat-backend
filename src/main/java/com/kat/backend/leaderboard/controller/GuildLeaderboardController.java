package com.kat.backend.leaderboard.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.leaderboard.client.LeaderboardBotClient;
import com.kat.backend.leaderboard.dto.LeaderboardSettingsDto;
import com.kat.backend.security.GuildAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/guilds/{guildId}/leaderboard")
@RequiredArgsConstructor
public class GuildLeaderboardController {

    private final LeaderboardBotClient leaderboardBotClient;

    @GetMapping("/settings")
    @GuildAdmin
    public ResponseEntity<ApiResponse<LeaderboardSettingsDto>> getSettings(@PathVariable String guildId) {
        return ResponseEntity.ok(ApiResponse.ok(leaderboardBotClient.getSettings(guildId)));
    }

    @PutMapping("/settings")
    @GuildAdmin
    public ResponseEntity<ApiResponse<LeaderboardSettingsDto>> updateSettings(
            @PathVariable String guildId,
            @RequestBody Map<String, Boolean> body) {
        Boolean showOnLeaderboard = body.get("showOnLeaderboard");
        if (showOnLeaderboard == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(ApiResponse.ok(leaderboardBotClient.updateSettings(guildId, showOnLeaderboard)));
    }
}
