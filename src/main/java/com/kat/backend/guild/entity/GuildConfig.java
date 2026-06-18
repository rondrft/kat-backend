package com.kat.backend.guild.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "guild_config")
public class GuildConfig {

    @Id
    @Column(name = "guild_id", nullable = false)
    private String guildId;

    @Column(nullable = false)
    private String prefix;

    @Column(nullable = false)
    private String locale;
}
