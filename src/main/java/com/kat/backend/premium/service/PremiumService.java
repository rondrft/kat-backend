package com.kat.backend.premium.service;

import com.kat.backend.premium.entity.PremiumGuild;
import com.kat.backend.premium.repository.PremiumGuildRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PremiumService {

    private static final int FREE_REACTION_ROLES_LIMIT = 8;

    @Value("${app.dev.guild-id:}")
    private String devGuildId;

    private final PremiumGuildRepository premiumGuildRepository;

    public boolean isPremium(String guildId) {
        if (guildId.equals(devGuildId)) return true;
        return premiumGuildRepository.findById(guildId)
                .map(p -> p.getExpiresAt() == null || p.getExpiresAt().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    public int getReactionRolesLimit(String guildId) {
        return isPremium(guildId) ? Integer.MAX_VALUE : FREE_REACTION_ROLES_LIMIT;
    }

    @Transactional
    public void activatePremium(String guildId, String plan, String userId) {
        PremiumGuild guild = premiumGuildRepository.findById(guildId)
                .orElseGet(() -> new PremiumGuild(guildId));

        LocalDateTime expiresAt = computeExpiry(plan, guild.getExpiresAt());
        guild.activate(plan, userId, expiresAt);
        premiumGuildRepository.save(guild);
    }

    private LocalDateTime computeExpiry(String plan, LocalDateTime currentExpiry) {
        LocalDateTime base = (currentExpiry != null && currentExpiry.isAfter(LocalDateTime.now()))
                ? currentExpiry
                : LocalDateTime.now();
        return switch (plan) {
            case "monthly" -> base.plusDays(31);
            case "yearly" -> base.plusDays(366);
            case "lifetime" -> null;
            default -> throw new IllegalArgumentException("Unknown plan: " + plan);
        };
    }
}