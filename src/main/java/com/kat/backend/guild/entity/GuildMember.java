package com.kat.backend.guild.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "guild_members", indexes = {
        @Index(name = "idx_guild_members_guild", columnList = "guild_id"),
        @Index(name = "idx_guild_members_user", columnList = "discord_user_id"),
        @Index(name = "idx_guild_members_joined", columnList = "joined_at")
})
@Data
public class GuildMember {

    @Id
    private String id;

    @Column(name = "guild_id")
    private String guildId;

    @Column(name = "discord_user_id")
    private String discordUserId;

    private String username;
    private String avatar;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "account_created_at")
    private LocalDateTime accountCreatedAt;

    @Column(name = "is_bot")
    private Boolean bot = false;

    @Column(name = "is_verified_bot")
    private Boolean verifiedBot = false;
}