package com.kat.backend.premium.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "premium_guilds")
@Getter
@NoArgsConstructor
public class PremiumGuild {

    @Id
    private String guildId;

    private LocalDateTime expiresAt;

    private LocalDateTime createdAt;
}