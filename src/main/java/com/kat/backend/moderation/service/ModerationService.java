package com.kat.backend.moderation.service;

import com.kat.backend.moderation.client.ModerationBotClient;
import com.kat.backend.moderation.dto.ModerationConfigDto;
import com.kat.backend.moderation.entity.ModerationConfig;
import com.kat.backend.moderation.mapper.ModerationConfigMapper;
import com.kat.backend.moderation.repository.ModerationConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ModerationConfigDto getConfig(String guildId) {
        return repository.findById(guildId)
                .map(mapper::toDto)
                .orElseGet(() -> mapper.defaultDto(guildId));
    }

    @Transactional
    public ModerationConfigDto saveConfig(String guildId, ModerationConfigDto dto) {
        ModerationConfig entity = repository.findById(guildId).orElseGet(() ->
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
}