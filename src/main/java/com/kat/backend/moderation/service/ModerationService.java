package com.kat.backend.moderation.service;

import com.kat.backend.moderation.client.ModerationBotClient;
import com.kat.backend.moderation.dto.*;
import com.kat.backend.moderation.entity.*;
import com.kat.backend.moderation.mapper.ModerationConfigMapper;
import com.kat.backend.moderation.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModerationService {

    private final ModerationConfigRepository repository;
    private final ModerationConfigMapper mapper;
    private final ModerationBotClient moderationBotClient;
    private final ModerationWhitelistRepository whitelistRepository;
    private final ModerationFilterRepository filterRepository;
    private final ModerationAutoPunishmentRepository autoPunishmentRepository;

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

    @Transactional(readOnly = true)
    public List<ModerationWhitelistDto> getWhitelist(String guildId) {
        return whitelistRepository.findByGuildId(guildId).stream()
                .map(this::toWhitelistDto).toList();
    }

    @Transactional
    public ModerationWhitelistDto addWhitelist(String guildId, ModerationWhitelistDto dto) {
        ModerationWhitelist entity = ModerationWhitelist.builder()
                .guildId(guildId)
                .channelId(dto.getChannelId())
                .userId(dto.getUserId())
                .categoryId(dto.getCategoryId())
                .ruleType(dto.getRuleType())
                .reason(dto.getReason())
                .build();
        return toWhitelistDto(whitelistRepository.save(entity));
    }

    @Transactional
    public void removeWhitelist(String guildId, UUID entryId) {
        ModerationWhitelist entry = whitelistRepository.findById(entryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!entry.getGuildId().equals(guildId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        whitelistRepository.delete(entry);
    }

    @Transactional(readOnly = true)
    public List<ModerationFilterDto> getFilters(String guildId) {
        return filterRepository.findByGuildId(guildId).stream()
                .map(this::toFilterDto).toList();
    }

    @Transactional
    public ModerationFilterDto addFilter(String guildId, ModerationFilterDto dto) {
        ModerationFilter entity = ModerationFilter.builder()
                .guildId(guildId)
                .pattern(dto.getPattern())
                .enabled(dto.isEnabled())
                .action(dto.getAction())
                .replacement(dto.getReplacement())
                .reason(dto.getReason())
                .build();
        return toFilterDto(filterRepository.save(entity));
    }

    @Transactional
    public ModerationFilterDto updateFilter(String guildId, UUID filterId, ModerationFilterDto dto) {
        ModerationFilter entity = filterRepository.findById(filterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!entity.getGuildId().equals(guildId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        entity.setPattern(dto.getPattern());
        entity.setEnabled(dto.isEnabled());
        entity.setAction(dto.getAction());
        entity.setReplacement(dto.getReplacement());
        entity.setReason(dto.getReason());
        return toFilterDto(filterRepository.save(entity));
    }

    @Transactional
    public void deleteFilter(String guildId, UUID filterId) {
        ModerationFilter entity = filterRepository.findById(filterId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!entity.getGuildId().equals(guildId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        filterRepository.delete(entity);
    }

    @Transactional(readOnly = true)
    public List<ModerationAutoPunishmentDto> getAutoPunishments(String guildId) {
        return autoPunishmentRepository.findByGuildId(guildId).stream()
                .map(this::toAutoPunishmentDto).toList();
    }

    @Transactional
    public List<ModerationAutoPunishmentDto> saveAutoPunishments(String guildId, List<ModerationAutoPunishmentDto> dtos) {
        autoPunishmentRepository.deleteByGuildId(guildId);
        List<ModerationAutoPunishment> entities = dtos.stream()
                .map(dto -> ModerationAutoPunishment.builder()
                        .guildId(guildId)
                        .ruleType(dto.getRuleType())
                        .threshold(dto.getThreshold())
                        .action(dto.getAction())
                        .timeoutMinutes(dto.getTimeoutMinutes())
                        .enabled(dto.isEnabled())
                        .build())
                .toList();
        return autoPunishmentRepository.saveAll(entities).stream()
                .map(this::toAutoPunishmentDto).toList();
    }

    @Transactional(readOnly = true)
    public Map<String, String> getLogChannel(String guildId) {
        return repository.findById(guildId)
                .map(cfg -> Map.of(
                        "logChannelId", cfg.getLogChannelId() != null ? cfg.getLogChannelId() : "",
                        "premiumLogChannelId", cfg.getPremiumLogChannelId() != null ? cfg.getPremiumLogChannelId() : ""
                ))
                .orElse(Map.of("logChannelId", "", "premiumLogChannelId", ""));
    }

    @Transactional
    public void saveLogChannel(String guildId, String logChannelId, String premiumLogChannelId) {
        ModerationConfig config = repository.findById(guildId).orElseGet(() ->
                ModerationConfig.builder().guildId(guildId).build());
        config.setLogChannelId(logChannelId != null && !logChannelId.isBlank() ? logChannelId : null);
        config.setPremiumLogChannelId(premiumLogChannelId != null && !premiumLogChannelId.isBlank() ? premiumLogChannelId : null);
        config.setUpdatedAt(Instant.now());
        repository.save(config);
    }

    private ModerationWhitelistDto toWhitelistDto(ModerationWhitelist e) {
        return ModerationWhitelistDto.builder()
                .id(e.getId()).guildId(e.getGuildId())
                .channelId(e.getChannelId()).userId(e.getUserId())
                .categoryId(e.getCategoryId()).ruleType(e.getRuleType())
                .reason(e.getReason()).createdAt(e.getCreatedAt())
                .build();
    }

    private ModerationFilterDto toFilterDto(ModerationFilter e) {
        return ModerationFilterDto.builder()
                .id(e.getId()).guildId(e.getGuildId())
                .pattern(e.getPattern()).enabled(e.isEnabled())
                .action(e.getAction()).replacement(e.getReplacement())
                .reason(e.getReason()).createdAt(e.getCreatedAt())
                .build();
    }

    private ModerationAutoPunishmentDto toAutoPunishmentDto(ModerationAutoPunishment e) {
        return ModerationAutoPunishmentDto.builder()
                .id(e.getId()).guildId(e.getGuildId())
                .ruleType(e.getRuleType()).threshold(e.getThreshold())
                .action(e.getAction()).timeoutMinutes(e.getTimeoutMinutes())
                .enabled(e.isEnabled())
                .build();
    }
}