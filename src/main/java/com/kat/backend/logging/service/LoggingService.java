package com.kat.backend.logging.service;

import com.kat.backend.logging.client.LoggingBotClient;
import com.kat.backend.logging.dto.LoggingConfigDto;
import com.kat.backend.logging.dto.LoggingEntryDto;
import com.kat.backend.logging.entity.LoggingConfig;
import com.kat.backend.logging.entity.LoggingEntry;
import com.kat.backend.logging.repository.LoggingConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoggingService {

    private final LoggingConfigRepository repository;
    private final LoggingBotClient botClient;

    @Transactional(readOnly = true)
    @Cacheable(value = "loggingConfigs", key = "#guildId", unless = "#result == null")
    public Optional<LoggingConfigDto> getConfig(String guildId) {
        return repository.findWithEntriesByGuildId(guildId).map(this::toDto);
    }

    @Transactional
    @CacheEvict(value = "loggingConfigs", key = "#guildId")
    public LoggingConfigDto saveConfig(String guildId, LoggingConfigDto dto) {
        LoggingConfig config = repository.findWithEntriesByGuildId(guildId)
                .orElseGet(() -> LoggingConfig.builder().guildId(guildId).build());

        config.setDefaultChannel(dto.getDefaultChannel());
        config.getEntries().clear();

        if (dto.getEntries() != null) {
            for (LoggingEntryDto entryDto : dto.getEntries()) {
                LoggingEntry entry = LoggingEntry.builder()
                        .config(config)
                        .logId(entryDto.getId())
                        .enabled(entryDto.isEnabled())
                        .channelId(entryDto.getChannelId() != null ? entryDto.getChannelId() : "")
                        .build();
                config.getEntries().add(entry);
            }
        }

        LoggingConfigDto saved = toDto(repository.save(config));
        String defaultChannel = dto.getDefaultChannel();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                botClient.notifyConfigSaved(guildId, defaultChannel);
            }
        });
        return saved;
    }

    private LoggingConfigDto toDto(LoggingConfig config) {
        LoggingConfigDto dto = new LoggingConfigDto();
        dto.setDefaultChannel(config.getDefaultChannel());
        List<LoggingEntryDto> entries = config.getEntries().stream()
                .map(e -> {
                    LoggingEntryDto ed = new LoggingEntryDto();
                    ed.setId(e.getLogId());
                    ed.setEnabled(e.isEnabled());
                    ed.setChannelId(e.getChannelId());
                    return ed;
                })
                .toList();
        dto.setEntries(entries);
        return dto;
    }
}