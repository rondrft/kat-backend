package com.kat.backend.guild.service;

import com.kat.backend.booster.repository.BoosterConfigRepository;
import com.kat.backend.booster.repository.BoosterCustomRoleRepository;
import com.kat.backend.guild.dto.GuildStatsDto;
import com.kat.backend.guild.repository.GuildAutoRolesConfigRepository;
import com.kat.backend.guild.repository.GuildTempVoiceConfigRepository;
import com.kat.backend.welcome.repository.WelcomeConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuildStatsService {

    @Qualifier("botRestClient")
    private final RestClient botRestClient;
    private final BoosterCustomRoleRepository boosterCustomRoleRepository;
    private final BoosterConfigRepository boosterConfigRepository;
    private final GuildAutoRolesConfigRepository autoRolesConfigRepository;
    private final WelcomeConfigRepository welcomeConfigRepository;
    private final GuildTempVoiceConfigRepository tempVoiceConfigRepository;

    public GuildStatsDto getStats(String guildId) {
        Map<String, Object> botStats = botRestClient.get()
                .uri("/internal/guilds/{guildId}/stats", guildId)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        long activeCustomRoles = boosterCustomRoleRepository.countByGuildId(guildId);

        boolean welcomeActive = welcomeConfigRepository.findById(guildId)
                .map(c -> c.isWelcomeEnabled() || c.isBoostEnabled())
                .orElse(false);

        boolean autoRolesActive = autoRolesConfigRepository.findById(guildId)
                .map(c -> c.isJoinEnabled() || c.isBoostEnabled() || c.isReactionEnabled())
                .orElse(false);

        boolean tempVoiceActive = tempVoiceConfigRepository.findById(guildId)
                .map(c -> c.isEnabled())
                .orElse(false);

        boolean boostersActive = boosterConfigRepository.findById(guildId)
                .map(c -> c.isEnabled())
                .orElse(false);

        return GuildStatsDto.builder()
                .totalMembers((Integer) botStats.get("totalMembers"))
                .totalTextChannels((Integer) botStats.get("totalTextChannels"))
                .totalVoiceChannels((Integer) botStats.get("totalVoiceChannels"))
                .totalCategories((Integer) botStats.get("totalCategories"))
                .totalRoles(((Number) botStats.get("totalRoles")).longValue())
                .totalEmojis((Integer) botStats.get("totalEmojis"))
                .serverCreatedAt((String) botStats.get("serverCreatedAt"))
                .boostLevel((Integer) botStats.get("boostLevel"))
                .boostCount((Integer) botStats.get("boostCount"))
                .activeCustomRoles(activeCustomRoles)
                .activeModules(Map.of(
                        "welcome", welcomeActive,
                        "autoRoles", autoRolesActive,
                        "tempVoice", tempVoiceActive,
                        "boosters", boostersActive
                ))
                .build();
    }
}