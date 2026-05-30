package com.kat.backend.booster.dto;

import com.kat.backend.booster.entity.BoosterCustomRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BoosterCustomRoleResponse {

    private UUID id;
    private String guildId;
    private String ownerDiscordId;
    private String discordRoleId;
    private String roleName;
    private String roleColor;
    private String roleEmoji;
    private LocalDateTime createdAt;

    public static BoosterCustomRoleResponse from(BoosterCustomRole entity) {
        return BoosterCustomRoleResponse.builder()
                .id(entity.getId())
                .guildId(entity.getGuildId())
                .ownerDiscordId(entity.getOwnerDiscordId())
                .discordRoleId(entity.getDiscordRoleId())
                .roleName(entity.getRoleName())
                .roleColor(entity.getRoleColor())
                .roleEmoji(entity.getRoleEmoji())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}