package com.kat.backend.moderation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.util.UUID;

@Entity
@Table(name = "moderation_auto_punishment",
        uniqueConstraints = @UniqueConstraint(columnNames = {"guild_id", "rule_type"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationAutoPunishment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "guild_id", nullable = false)
    private String guildId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ModerationRuleType ruleType;

    @Column(name = "threshold", nullable = false)
    @Builder.Default
    private int threshold = 5;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ModerationAction action;

    @Column(name = "timeout_minutes")
    private Integer timeoutMinutes;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private boolean enabled = true;
}
