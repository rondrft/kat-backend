package com.kat.backend.moderation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "moderation_filter", indexes = {
        @Index(name = "idx_moderation_filter_guild", columnList = "guild_id"),
        @Index(name = "idx_moderation_filter_enabled", columnList = "guild_id, enabled")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "guild_id", nullable = false)
    private String guildId;

    @Column(name = "pattern", nullable = false)
    private String pattern;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private boolean enabled = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ModerationAction action;

    @Column(name = "replacement")
    private String replacement;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}
