package com.kat.backend.logging.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "logging_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(LoggingEntryId.class)
public class LoggingEntry {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guild_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private LoggingConfig config;

    @Id
    @Column(name = "log_id", length = 50, nullable = false)
    private String logId;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "channel_id", length = 20)
    private String channelId;
}