package com.kat.backend.guild.dto;

import lombok.Data;

import java.util.List;

@Data
public class DashboardAccessRequest {
    private List<String> allowedUserIds;
    private List<String> allowedRoleIds;
}
