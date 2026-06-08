package com.kat.backend.guild.service;

import com.kat.backend.common.ApiResponse;
import com.kat.backend.common.EmojiUtil;
import com.kat.backend.guild.dto.*;
import com.kat.backend.guild.entity.GuildAutoRolesConfig;
import com.kat.backend.guild.entity.ReactionRoleMapping;
import com.kat.backend.guild.repository.GuildAutoRolesConfigRepository;
import com.kat.backend.premium.service.PremiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AutoRolesService {

    private final GuildAutoRolesConfigRepository repository;
    private final BotGuildService botGuildService;
    private final PremiumService premiumService;

    @Transactional(readOnly = true)
    @Cacheable(value = "autoRolesConfigs", key = "#guildId", unless = "#result == null")
    public AutoRolesConfigResponse getConfig(String guildId) {
        GuildAutoRolesConfig config = repository.findById(guildId)
                .orElse(new GuildAutoRolesConfig());
        return toResponse(config);
    }

    @Transactional
    @CacheEvict(value = "autoRolesConfigs", key = "#guildId")
    public AutoRolesConfigResponse saveConfig(String guildId, AutoRolesConfigRequest request) {
        GuildAutoRolesConfig config = repository.findById(guildId)
                .orElseGet(() -> {
                    GuildAutoRolesConfig c = new GuildAutoRolesConfig();
                    c.setGuildId(guildId);
                    return c;
                });

        config.setJoinEnabled(request.isJoinEnabled());
        config.setJoinRoleIds(request.getJoinRoleIds() != null
                ? new ArrayList<>(request.getJoinRoleIds()) : new ArrayList<>());

        config.setBoostEnabled(request.isBoostEnabled());
        config.setBoostRoleIds(request.getBoostRoleIds() != null
                ? new ArrayList<>(request.getBoostRoleIds()) : new ArrayList<>());

        config.setReactionEnabled(request.isReactionEnabled());
        config.setReactionChannelId(request.getReactionChannelId());
        config.setReactionMessageContent(request.getReactionMessageContent());
        config.setReactionUseEmbed(request.isReactionUseEmbed());
        config.setReactionEmbedTitle(request.getReactionEmbedTitle());
        config.setReactionEmbedColor(request.getReactionEmbedColor());

        if (request.getReactionMappings() != null) {
            int limit = premiumService.getReactionRolesLimit(guildId);
            if (request.getReactionMappings().size() > limit) {
                throw new IllegalArgumentException(
                        "Free guilds can only configure up to " + limit + " reaction role mappings.");
            }
            config.setReactionMappings(new ArrayList<>(request.getReactionMappings().stream()
                    .map(dto -> {
                        ReactionRoleMapping m = new ReactionRoleMapping();
                        m.setEmoji(EmojiUtil.normalize(dto.getEmoji()));
                        m.setRoleId(dto.getRoleId());
                        return m;
                    })
                    .toList()));
        }

        if (request.isReactionEnabled()
                && request.getReactionChannelId() != null
                && request.getReactionMessageContent() != null
                && config.getReactionMappings() != null
                && !config.getReactionMappings().isEmpty()) {

            SyncReactionPanelRequest syncRequest = new SyncReactionPanelRequest(
                    request.getReactionChannelId(),
                    request.getReactionMessageContent(),
                    config.getReactionMessageId(),
                    config.getReactionMappings().stream()
                            .map(m -> new ReactionRoleMappingDto(m.getEmoji(), m.getRoleId()))
                            .toList(),
                    request.isReactionUseEmbed(),
                    request.getReactionEmbedTitle(),
                    request.getReactionEmbedColor()
            );

            String messageId = botGuildService.syncReactionPanel(guildId, syncRequest);
            if (messageId != null) {
                config.setReactionMessageId(messageId);
            }
        }

        return toResponse(repository.save(config));
    }

    private AutoRolesConfigResponse toResponse(GuildAutoRolesConfig config) {
        AutoRolesConfigResponse response = new AutoRolesConfigResponse();
        response.setJoinEnabled(config.isJoinEnabled());
        response.setJoinRoleIds(new ArrayList<>(config.getJoinRoleIds()));
        response.setBoostEnabled(config.isBoostEnabled());
        response.setBoostRoleIds(new ArrayList<>(config.getBoostRoleIds()));
        response.setReactionEnabled(config.isReactionEnabled());
        response.setReactionChannelId(config.getReactionChannelId());
        response.setReactionMessageContent(config.getReactionMessageContent());
        response.setReactionMessageId(config.getReactionMessageId());
        response.setReactionUseEmbed(config.isReactionUseEmbed());
        response.setReactionEmbedTitle(config.getReactionEmbedTitle());
        response.setReactionEmbedColor(config.getReactionEmbedColor());
        response.setReactionMappings(
                config.getReactionMappings() == null ? new ArrayList<>() :
                        config.getReactionMappings().stream()
                                .map(m -> new ReactionRoleMappingDto(m.getEmoji(), m.getRoleId()))
                                .toList()
        );
        return response;
    }
}