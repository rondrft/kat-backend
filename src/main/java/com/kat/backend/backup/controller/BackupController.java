package com.kat.backend.backup.controller;

import com.kat.backend.backup.client.BackupBotClient;
import com.kat.backend.backup.dto.BackupDto;
import com.kat.backend.backup.dto.CreateBackupRequest;
import com.kat.backend.common.ApiResponse;
import com.kat.backend.security.GuildAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guilds/{guildId}/backups")
@RequiredArgsConstructor
public class BackupController {

    private static final int MAX_NAME_LENGTH = 100;

    private final BackupBotClient backupBotClient;

    @GetMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<List<BackupDto>>> listBackups(@PathVariable String guildId) {
        return ResponseEntity.ok(ApiResponse.ok(backupBotClient.listBackups(guildId)));
    }

    @PostMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<BackupDto>> createBackup(
            @PathVariable String guildId,
            @RequestBody CreateBackupRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Backup name is required"));
        }
        if (request.name().length() > MAX_NAME_LENGTH) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Backup name must be 100 characters or fewer"));
        }
        BackupDto created = backupBotClient.createBackup(guildId, request.name().trim());
        if (created == null) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Failed to create backup"));
        }
        return ResponseEntity.ok(ApiResponse.ok(created));
    }

    @PostMapping("/{id}/restore")
    @GuildAdmin
    public ResponseEntity<ApiResponse<Void>> restoreBackup(
            @PathVariable String guildId,
            @PathVariable String id) {
        boolean started = backupBotClient.restoreBackup(guildId, id);
        if (!started) {
            return ResponseEntity.internalServerError().body(ApiResponse.error("Could not start restore"));
        }
        return ResponseEntity.accepted().body(ApiResponse.ok(null));
    }

    @DeleteMapping("/{id}")
    @GuildAdmin
    public ResponseEntity<ApiResponse<Void>> deleteBackup(
            @PathVariable String guildId,
            @PathVariable String id) {
        backupBotClient.deleteBackup(guildId, id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
