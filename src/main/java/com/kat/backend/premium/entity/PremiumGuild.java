package com.kat.backend.premium.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "premium_guilds")
@Getter
@Setter
@NoArgsConstructor
public class PremiumGuild {

    @Id
    private String guildId;

    private String plan;
    private String purchasedByUserId;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PremiumGuild(String guildId) {
        this.guildId = guildId;
        this.createdAt = LocalDateTime.now();
    }

    public void activate(String plan, String userId, LocalDateTime expiresAt) {
        this.plan = plan;
        this.purchasedByUserId = userId;
        this.expiresAt = expiresAt;
        this.updatedAt = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
