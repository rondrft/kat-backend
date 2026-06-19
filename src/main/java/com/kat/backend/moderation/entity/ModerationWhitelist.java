package com.kat.backend.moderation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "moderation_whitelist")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationWhitelist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "guild_id", nullable = false)
    private String guildId;

    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "category_id")
    private String categoryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ModerationRuleType ruleType;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}
