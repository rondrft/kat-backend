package com.kat.backend.booster.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booster_config")
public class BoosterConfig {

    @Id
    @Column(name = "guild_id", nullable = false)
    private String guildId;

    @Column(nullable = false)
    private boolean enabled;

    @Column(name = "required_boosts", nullable = false)
    private int requiredBoosts;

    @Column(name = "allow_invites", nullable = false)
    private boolean allowInvites;
}