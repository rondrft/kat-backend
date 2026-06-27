package com.kat.backend.leaderboard.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.leaderboard.client.LeaderboardBotClient;
import com.kat.backend.leaderboard.dto.ServerLeaderboardEntryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardBotClient leaderboardBotClient;

    @GetMapping("/servers")
    public ResponseEntity<ApiResponse<List<ServerLeaderboardEntryDto>>> getServers() {
        return ResponseEntity.ok(ApiResponse.ok(leaderboardBotClient.getLeaderboard()));
    }
}
