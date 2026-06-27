package com.kat.backend.moderation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.type.PostgreSQLEnumJdbcType;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "moderation_logs", indexes = {
        @Index(name = "idx_moderation_logs_guild", columnList = "guild_id"),
        @Index(name = "idx_moderation_logs_user", columnList = "user_id"),
        @Index(name = "idx_moderation_logs_created", columnList = "created_at")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModerationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "guild_id", nullable = false)
    private String guildId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "message_id")
    private String messageId;

    @Column(name = "channel_id")
    private String channelId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ModerationRuleType ruleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ModerationAction action;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "content_preview", length = 200)
    private String contentPreview;

    @Column(name = "success", nullable = false)
    private boolean success;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}