package com.kat.backend.booster.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booster_custom_roles")
public class BoosterCustomRole {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "guild_id", nullable = false)
    private String guildId;

    @Column(name = "owner_discord_id", nullable = false)
    private String ownerDiscordId;

    @Column(name = "discord_role_id")
    private String discordRoleId;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "role_color")
    private String roleColor;

    @Column(name = "role_emoji")
    private String roleEmoji;

    @Column(name = "guild_emoji_id")
    private String guildEmojiId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}