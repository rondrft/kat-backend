package com.kat.backend.moderation.dto;

import lombok.Data;
import java.util.List;

@Data
public class NukeConfigDto {
    private String allowedRoleId;
    private List<String> allowedUserIds;
}