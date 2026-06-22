package com.kat.backend.guild.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DashboardAccessResponse {
    private List<String> allowedUserIds;
    private List<String> allowedRoleIds;
}
