package com.kat.backend.guild.service;

import com.kat.backend.guild.dto.TempVoiceConfigRequest;
import com.kat.backend.guild.dto.TempVoiceConfigResponse;
import com.kat.backend.guild.entity.GuildTempVoiceConfig;
import com.kat.backend.guild.repository.GuildTempVoiceConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TempVoiceService {

    private final GuildTempVoiceConfigRepository repository;
    private final BotGuildService botGuildService;

    @Cacheable(value = "tempVoiceConfigs", key = "#guildId", unless = "#result == null")
    public TempVoiceConfigResponse getConfig(String guildId) {
        return repository.findById(guildId)
                .map(this::toResponse)
                .orElse(defaultResponse(guildId));
    }

    @Transactional
    @CacheEvict(value = "tempVoiceConfigs", key = "#guildId")
    public TempVoiceConfigResponse saveConfig(String guildId, TempVoiceConfigRequest request) {
        GuildTempVoiceConfig config = repository.findById(guildId)
                .orElse(GuildTempVoiceConfig.builder().guildId(guildId).build());

        config.setEnabled(request.isEnabled());
        config.setChannelNameTemplate(request.getChannelNameTemplate());
        config.setUserLimit(request.getUserLimit());
        config.setDeleteDelaySeconds(request.getDeleteDelaySeconds());
        config.setLockedToOwner(request.isLockedToOwner());

        if (request.isEnabled()) {
            Map<String, Object> provisionRequest = Map.of(
                    "categoryId", request.getCategoryId() != null ? request.getCategoryId() : "DEFAULT",
                    "channelNameTemplate", request.getChannelNameTemplate(),
                    "existingHubChannelId", config.getHubChannelId() != null ? config.getHubChannelId() : ""
            );

            Map<String, String> provisioned = botGuildService.provisionVoice(guildId, provisionRequest);
            config.setCategoryId(provisioned.get("categoryId"));
            config.setHubChannelId(provisioned.get("hubChannelId"));
        }

        return toResponse(repository.save(config));
    }

    private TempVoiceConfigResponse toResponse(GuildTempVoiceConfig config) {
        return TempVoiceConfigResponse.builder()
                .guildId(config.getGuildId())
                .enabled(config.isEnabled())
                .categoryId(config.getCategoryId())
                .hubChannelId(config.getHubChannelId())
                .channelNameTemplate(config.getChannelNameTemplate())
                .userLimit(config.getUserLimit())
                .deleteDelaySeconds(config.getDeleteDelaySeconds())
                .lockedToOwner(config.isLockedToOwner())
                .build();
    }

    private TempVoiceConfigResponse defaultResponse(String guildId) {
        return TempVoiceConfigResponse.builder()
                .guildId(guildId)
                .enabled(false)
                .channelNameTemplate("{username}'s Channel")
                .userLimit(0)
                .deleteDelaySeconds(5)
                .lockedToOwner(true)
                .build();
    }

    @Transactional
    @CacheEvict(value = "tempVoiceConfigs", key = "#guildId")
    public void deleteAllChannels(String guildId) {
        botGuildService.deleteAllTempVoiceChannels(guildId);
    }

}