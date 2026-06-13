package com.kat.backend.audit.controller;

import com.kat.backend.audit.client.AuditBotClient;
import com.kat.backend.audit.dto.AuditLogDto;
import com.kat.backend.audit.dto.RankingEntryDto;
import com.kat.backend.common.ApiResponse;
import com.kat.backend.security.GuildAdmin;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guilds/{guildId}")
@RequiredArgsConstructor
public class AuditController {

    private final AuditBotClient auditBotClient;

    @GetMapping("/audit-logs")
    @GuildAdmin
    public ResponseEntity<ApiResponse<List<AuditLogDto>>> getAuditLogs(
            @PathVariable String guildId,
            @Max(200) @RequestParam(defaultValue = "50") int limit) {
        List<AuditLogDto> logs = auditBotClient.getAuditLogs(guildId, limit);
        return ResponseEntity.ok(ApiResponse.ok(logs));
    }

    @GetMapping("/ranking")
    @GuildAdmin
    public ResponseEntity<ApiResponse<List<RankingEntryDto>>> getRanking(
            @PathVariable String guildId,
            @Max(200) @RequestParam(defaultValue = "50") int limit) {
        List<RankingEntryDto> ranking = auditBotClient.getRanking(guildId, limit);
        return ResponseEntity.ok(ApiResponse.ok(ranking));
    }
}