package com.kat.backend.tickets.service;

import com.kat.backend.tickets.dto.TicketConfigRequest;
import com.kat.backend.tickets.dto.TicketConfigResponse;
import com.kat.backend.tickets.entity.TicketConfig;
import com.kat.backend.tickets.repository.TicketConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketConfigRepository repository;
    private final TicketBotClient ticketBotClient;

    @Cacheable(value = "ticketConfigs", key = "#guildId", unless = "#result == null")
    public TicketConfigResponse getConfig(String guildId) {
        return repository.findById(guildId)
                .map(this::toResponse)
                .orElse(null);
    }

    @Transactional
    @CacheEvict(value = "ticketConfigs", key = "#guildId")
    public TicketConfigResponse saveConfig(String guildId, TicketConfigRequest request) {
        TicketConfig config = repository.findById(guildId)
                .orElseGet(() -> {
                    TicketConfig c = new TicketConfig();
                    c.setGuildId(guildId);
                    return c;
                });

        boolean wasEnabled = config.isEnabled();
        boolean nowEnabled = Boolean.TRUE.equals(request.getEnabled());

        config.setEnabled(nowEnabled);
        config.setPanelChannelId(request.getPanelChannelId());
        config.setCategoryId(request.getCategoryId());
        config.setButtonLabel(request.getButtonLabel());
        config.setButtonStyle(request.getButtonStyle());
        config.setEmbedTitle(request.getEmbedTitle());
        config.setEmbedDescription(request.getEmbedDescription());
        config.setEmbedColor(request.getEmbedColor());
        config.setTranscriptEnabled(Boolean.TRUE.equals(request.getTranscriptEnabled()));
        config.setAllowUserToClose(Boolean.TRUE.equals(request.getAllowUserToClose()));
        config.setMaxOpenTicketsPerUser(request.getMaxOpenTicketsPerUser());
        config.setTicketChannelNameTemplate(request.getTicketChannelNameTemplate());
        config.setWelcomeMessage(request.getWelcomeMessage());
        config.setSupportRoleIds(new ArrayList<>(request.getSupportRoleIds()));

        TicketConfig saved = repository.save(config);
        TicketConfigResponse response = toResponse(saved);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                if (nowEnabled) {
                    ticketBotClient.createOrUpdatePanel(guildId, response);
                } else if (wasEnabled) {
                    ticketBotClient.deletePanel(guildId);
                }
            }
        });
        return response;
    }

    @CacheEvict(value = "ticketConfigs", key = "#guildId")
    public void resetSystem(String guildId) {
        ticketBotClient.deleteAllTickets(guildId);
        repository.deleteById(guildId);
    }

    private TicketConfigResponse toResponse(TicketConfig config) {
        return TicketConfigResponse.builder()
                .guildId(config.getGuildId())
                .enabled(config.isEnabled())
                .panelChannelId(config.getPanelChannelId())
                .panelMessageId(config.getPanelMessageId())
                .categoryId(config.getCategoryId())
                .buttonLabel(config.getButtonLabel())
                .buttonStyle(config.getButtonStyle())
                .embedTitle(config.getEmbedTitle())
                .embedDescription(config.getEmbedDescription())
                .embedColor(config.getEmbedColor())
                .transcriptEnabled(config.isTranscriptEnabled())
                .allowUserToClose(config.isAllowUserToClose())
                .maxOpenTicketsPerUser(config.getMaxOpenTicketsPerUser())
                .ticketChannelNameTemplate(config.getTicketChannelNameTemplate())
                .welcomeMessage(config.getWelcomeMessage())
                .supportRoleIds(new ArrayList<>(config.getSupportRoleIds()))
                .build();
    }
}
