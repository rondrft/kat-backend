package com.kat.backend.guild.controller;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.guild.dto.DashboardAccessRequest;
import com.kat.backend.guild.dto.DashboardAccessResponse;
import com.kat.backend.guild.service.DashboardAccessService;
import com.kat.backend.security.GuildAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guilds/{guildId}/settings/access")
@RequiredArgsConstructor
public class DashboardAccessController {

    private final DashboardAccessService dashboardAccessService;

    @GetMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<DashboardAccessResponse>> getAccess(
            @PathVariable String guildId) {

        DashboardAccessResponse response = new DashboardAccessResponse(
                dashboardAccessService.getAllowedUserIds(guildId),
                dashboardAccessService.getAllowedRoleIds(guildId)
        );
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PutMapping
    @GuildAdmin
    public ResponseEntity<ApiResponse<DashboardAccessResponse>> saveAccess(
            @PathVariable String guildId,
            @RequestBody DashboardAccessRequest request) {

        List<String> userIds = request.getAllowedUserIds() != null ? request.getAllowedUserIds() : List.of();
        List<String> roleIds = request.getAllowedRoleIds() != null ? request.getAllowedRoleIds() : List.of();

        dashboardAccessService.saveAccess(guildId, userIds, roleIds);

        DashboardAccessResponse response = new DashboardAccessResponse(userIds, roleIds);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }
}
