package com.kat.backend.booster.service;

import com.kat.backend.booster.client.BoosterBotClient;
import com.kat.backend.booster.dto.BoosterConfigRequest;
import com.kat.backend.booster.dto.BoosterConfigResponse;
import com.kat.backend.booster.dto.BoosterCustomRoleResponse;
import com.kat.backend.booster.entity.BoosterConfig;
import com.kat.backend.booster.repository.BoosterConfigRepository;
import com.kat.backend.booster.repository.BoosterCustomRoleRepository;
import com.kat.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoosterService {

    private final BoosterCustomRoleRepository boosterCustomRoleRepository;
    private final BoosterConfigRepository boosterConfigRepository;
    private final BoosterBotClient boosterBotClient;

    public int syncBoosters(String guildId) {
        return boosterBotClient.syncBoosters(guildId);
    }

    @Cacheable(value = "boosterCustomRoles", key = "#guildId + ':' + #discordId", unless = "#result == null")
    public BoosterCustomRoleResponse getMyCustomRole(String guildId, String discordId) {
        return boosterCustomRoleRepository
                .findByGuildIdAndOwnerDiscordId(guildId, discordId)
                .map(BoosterCustomRoleResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No custom role found for user " + discordId + " in guild " + guildId));
    }

    public Page<BoosterCustomRoleResponse> getAllCustomRoles(String guildId, Pageable pageable) {
        return boosterCustomRoleRepository
                .findAllByGuildId(guildId, pageable)
                .map(BoosterCustomRoleResponse::from);
    }

    @Cacheable(value = "boosterSettings", key = "#guildId", unless = "#result == null")
    public BoosterConfigResponse getSettings(String guildId) {
        return boosterConfigRepository.findById(guildId)
                .map(BoosterConfigResponse::from)
                .orElseGet(() -> BoosterConfigResponse.builder()
                        .guildId(guildId)
                        .enabled(false)
                        .requiredBoosts(2)
                        .allowInvites(true)
                        .build());
    }

    @CacheEvict(value = {"boosterSettings", "boosterCustomRoles"}, key = "#guildId")
    public BoosterConfigResponse updateSettings(String guildId, BoosterConfigRequest request) {
        BoosterConfig config = boosterConfigRepository.findById(guildId)
                .orElse(BoosterConfig.builder().guildId(guildId).build());

        config.setEnabled(request.isEnabled());
        config.setRequiredBoosts(request.getRequiredBoosts());
        config.setAllowInvites(request.isAllowInvites());

        return BoosterConfigResponse.from(boosterConfigRepository.save(config));
    }
}