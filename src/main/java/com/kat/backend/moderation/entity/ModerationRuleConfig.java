package com.kat.backend.moderation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;
import java.util.UUID;

@Entity
@Table(name = "moderation_rule_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationRuleConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guild_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ModerationConfig config;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ModerationRuleType ruleType;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private boolean enabled = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Builder.Default
    private ModerationAction action = ModerationAction.MONITOR;

    @Column(name = "threshold", nullable = false)
    @Builder.Default
    private int threshold = 5;
}