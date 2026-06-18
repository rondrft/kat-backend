package com.kat.backend.moderation.service;

import com.kat.backend.moderation.client.ModerationBotClient;
import com.kat.backend.moderation.dto.ModPermissionDto;
import com.kat.backend.moderation.dto.ModerationConfigDto;
import com.kat.backend.moderation.dto.NukeConfigDto;
import com.kat.backend.moderation.dto.PurgeConfigDto;
import com.kat.backend.moderation.entity.ModerationConfig;
import com.kat.backend.moderation.mapper.ModerationConfigMapper;
import com.kat.backend.moderation.repository.ModerationConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModerationService {

    private final ModerationConfigRepository repository;
    private final ModerationConfigMapper mapper;
    private final ModerationBotClient moderationBotClient;

    @Transactional(readOnly = true)
    @Cacheable(value = "moderationConfigs", key = "#guildId", unless = "#result == null")
    public ModerationConfigDto getConfig(String guildId) {
        return repository.findWithRulesByGuildId(guildId)
                .map(mapper::toDto)
                .orElseGet(() -> mapper.defaultDto(guildId));
    }

    @Transactional
    @CacheEvict(value = "moderationConfigs", key = "#guildId")
    public ModerationConfigDto saveConfig(String guildId, ModerationConfigDto dto) {
        ModerationConfig entity = repository.findWithRulesByGuildId(guildId).orElseGet(() ->
                ModerationConfig.builder()
                        .guildId(guildId)
                        .build()
        );
        mapper.updateEntity(entity, dto);
        entity.setUpdatedAt(Instant.now());
        ModerationConfig saved = repository.save(entity);
        log.info("Moderation config saved for guild {}", guildId);
        moderationBotClient.invalidateModerationCache(guildId);
        return mapper.toDto(saved);
    }

    public ModPermissionDto getPermissions(String guildId) {
        return moderationBotClient.getPermissions(guildId);
    }

    @Transactional
    public ModPermissionDto savePermissions(String guildId, ModPermissionDto dto) {
        moderationBotClient.savePermissions(guildId, dto);
        return dto;
    }

    public PurgeConfigDto getPurgeConfig(String guildId) {
        return moderationBotClient.getPurgeConfig(guildId);
    }

    @Transactional
    public PurgeConfigDto savePurgeConfig(String guildId, PurgeConfigDto dto) {
        moderationBotClient.savePurgeConfig(guildId, dto);
        return dto;
    }

    public NukeConfigDto getNukeConfig(String guildId) {
        return moderationBotClient.getNukeConfig(guildId);
    }

    @Transactional
    public NukeConfigDto saveNukeConfig(String guildId, NukeConfigDto dto) {
        moderationBotClient.saveNukeConfig(guildId, dto);
        return dto;
    }
}