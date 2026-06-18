package com.kat.backend.audit.controller;

import com.kat.backend.audit.client.AuditBotClient;
import com.kat.backend.audit.dto.AuditLogDto;
import com.kat.backend.audit.dto.RankingEntryDto;
import com.kat.backend.common.ApiResponse;
import com.kat.backend.security.GuildAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guilds/{guildId}")
@RequiredArgsConstructor
public class AuditController {

    private final AuditBotClient auditBotClient;

    @GetMapping("/audit-logs")
    @GuildAdmin
    public ResponseEntity<ApiResponse<Page<AuditLogDto>>> getAuditLogs(
            @PathVariable String guildId,
            @PageableDefault(size = 50) Pageable pageable) {
        Page<AuditLogDto> logs = auditBotClient.getAuditLogs(guildId, pageable);
        return ResponseEntity.ok(ApiResponse.ok(logs));
    }

    @GetMapping("/ranking")
    @GuildAdmin
    public ResponseEntity<ApiResponse<Page<RankingEntryDto>>> getRanking(
            @PathVariable String guildId,
            @PageableDefault(size = 50) Pageable pageable) {
        Page<RankingEntryDto> ranking = auditBotClient.getRanking(guildId, pageable);
        return ResponseEntity.ok(ApiResponse.ok(ranking));
    }
}