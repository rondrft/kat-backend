package com.kat.backend.guild.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.guild.dto.DashboardAccessRequest;
import com.kat.backend.guild.dto.DashboardAccessResponse;
import com.kat.backend.guild.service.DashboardAccessService;
import com.kat.backend.security.GuildAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/guilds/{guildId}/settings/access")
@RequiredArgsConstructor
public class DashboardAccessController {

    private final DashboardAccessService dashboardAccessService;

    @GetMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<DashboardAccessResponse>> getAccess(
            @PathVariable String guildId) {
        try {
            DashboardAccessResponse response = new DashboardAccessResponse(
                    dashboardAccessService.getAllowedUserIds(guildId),
                    dashboardAccessService.getAllowedRoleIds(guildId)
            );
            return ResponseEntity.ok(ApiResponse.ok(response));
        } catch (DataAccessException e) {
            log.error("Dashboard access read error for guild {}: {}", guildId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al leer configuración de acceso"));
        }
    }

    @PutMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<DashboardAccessResponse>> saveAccess(
            @PathVariable String guildId,
            @RequestBody DashboardAccessRequest request) {
        try {
            List<String> userIds = request.getAllowedUserIds() != null ? request.getAllowedUserIds() : List.of();
            List<String> roleIds = request.getAllowedRoleIds() != null ? request.getAllowedRoleIds() : List.of();

            dashboardAccessService.saveAccess(guildId, userIds, roleIds);

            DashboardAccessResponse response = new DashboardAccessResponse(userIds, roleIds);
            return ResponseEntity.ok(ApiResponse.ok(response));
        } catch (DataAccessException e) {
            log.error("Dashboard access save error for guild {}: {}", guildId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al guardar configuración de acceso"));
        }
    }
}
