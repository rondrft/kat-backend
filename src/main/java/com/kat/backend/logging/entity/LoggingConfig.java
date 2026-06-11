package com.kat.backend.logging.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "logging_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoggingConfig {

    @Id
    @Column(name = "guild_id")
    private String guildId;

    @Column(name = "default_channel")
    private String defaultChannel;

    @OneToMany(mappedBy = "config", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<LoggingEntry> entries = new ArrayList<>();
}