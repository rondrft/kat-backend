package com.kat.backend.moderation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "moderation_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationConfig {

    @Id
    @Column(name = "guild_id")
    private String guildId;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private boolean enabled = false;

    @Column(name = "strictness", nullable = false)
    @Builder.Default
    private int strictness = 50;

    @Column(name = "default_timeout_minutes", nullable = false)
    @Builder.Default
    private int defaultTimeoutMinutes = 10;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "config", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ModerationRuleConfig> rules = new ArrayList<>();
}