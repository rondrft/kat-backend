package com.kat.backend.moderation.mapper;

import com.kat.backend.moderation.dto.ModerationConfigDto;
import com.kat.backend.moderation.dto.ModerationRuleDto;
import com.kat.backend.moderation.entity.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ModerationConfigMapper {

    private static final List<ModerationRuleDto> DEFAULT_RULES = List.of(
            buildDefaultRule(ModerationRuleType.SPAM,           ModerationAction.DELETE,   6,  null),
            buildDefaultRule(ModerationRuleType.LINKS,          ModerationAction.DELETE,   2,  null),
            buildDefaultRule(ModerationRuleType.INVITES,        ModerationAction.DELETE,   1,  null),
            buildDefaultRule(ModerationRuleType.MENTIONS,       ModerationAction.TIMEOUT,  5,  null),
            buildDefaultRule(ModerationRuleType.CAPS,           ModerationAction.MONITOR, 80,  null),
            buildDefaultRule(ModerationRuleType.REPETITION,     ModerationAction.WARN,     5,  null),
            buildDefaultRule(ModerationRuleType.WALL_OF_TEXT,   ModerationAction.WARN,   500,  null),
            buildDefaultRule(ModerationRuleType.NEWLINES,       ModerationAction.DELETE,   5,  null),
            buildDefaultRule(ModerationRuleType.SPOILERS,       ModerationAction.WARN,     5,  null),
            buildDefaultRule(ModerationRuleType.EVERYONE_HERE,  ModerationAction.TIMEOUT,  1,  null),
            buildDefaultRule(ModerationRuleType.FORMATTING,     ModerationAction.WARN,     5,  null),
            buildDefaultRule(ModerationRuleType.EMOJIS,         ModerationAction.WARN,     5,  null),
            buildDefaultRule(ModerationRuleType.BAD_WORDS,      ModerationAction.DELETE,   1,  null),
            buildDefaultRule(ModerationRuleType.PHISHING,       ModerationAction.DELETE,   1,  null),
            buildDefaultRule(ModerationRuleType.MASS_MENTION,   ModerationAction.WARN,     5,  null),
            buildDefaultRule(ModerationRuleType.IMAGE_SPAM,     ModerationAction.DELETE,   3,  null),
            buildDefaultRule(ModerationRuleType.COPY_PASTA,     ModerationAction.DELETE,   1,  null),
            buildDefaultRule(ModerationRuleType.ACCOUNT_AGE,    ModerationAction.TIMEOUT,  7,  null),
            buildDefaultRule(ModerationRuleType.JOIN_RAID,      ModerationAction.KICK,    10,  null),
            buildDefaultRule(ModerationRuleType.CHANNEL_RAID,   ModerationAction.MONITOR,  5,  null),
            buildDefaultRule(ModerationRuleType.ROLE_RAID,      ModerationAction.MONITOR,  5,  null)
    );

    private static ModerationRuleDto buildDefaultRule(
            ModerationRuleType type,
            ModerationAction action,
            int threshold,
            Integer timeoutMinutes
    ) {
        return ModerationRuleDto.builder()
                .id(type)
                .enabled(false)
                .action(action)
                .threshold(threshold)
                .timeoutMinutes(timeoutMinutes)
                .build();
    }

    public ModerationConfigDto toDto(ModerationConfig entity) {
        Map<ModerationRuleType, ModerationRuleConfig> ruleMap = entity.getRules().stream()
                .collect(Collectors.toMap(ModerationRuleConfig::getRuleType, r -> r));

        List<ModerationRuleDto> ruleDtos = Arrays.stream(ModerationRuleType.values())
                .map(type -> {
                    ModerationRuleConfig rule = ruleMap.get(type);
                    if (rule != null) {
                        return ModerationRuleDto.builder()
                                .id(rule.getRuleType())
                                .enabled(rule.isEnabled())
                                .action(rule.getAction())
                                .threshold(rule.getThreshold())
                                .timeoutMinutes(rule.getTimeoutMinutes())
                                .build();
                    }
                    return DEFAULT_RULES.stream()
                            .filter(d -> d.getId() == type)
                            .findFirst()
                            .orElseThrow();
                })
                .collect(Collectors.toList());

        return ModerationConfigDto.builder()
                .guildId(entity.getGuildId())
                .enabled(entity.isEnabled())
                .strictness(entity.getStrictness())
                .defaultTimeoutMinutes(entity.getDefaultTimeoutMinutes())
                .rules(ruleDtos)
                .logChannelId(entity.getLogChannelId())
                .premiumLogChannelId(entity.getPremiumLogChannelId())
                .build();
    }

    public ModerationConfigDto defaultDto(String guildId) {
        return ModerationConfigDto.builder()
                .guildId(guildId)
                .enabled(false)
                .strictness(50)
                .defaultTimeoutMinutes(10)
                .rules(DEFAULT_RULES)
                .build();
    }

    public void updateEntity(ModerationConfig entity, ModerationConfigDto dto) {
        entity.setEnabled(dto.isEnabled());
        entity.setStrictness(dto.getStrictness());
        entity.setDefaultTimeoutMinutes(dto.getDefaultTimeoutMinutes());
        entity.setLogChannelId(dto.getLogChannelId());
        entity.setPremiumLogChannelId(dto.getPremiumLogChannelId());

        Map<ModerationRuleType, ModerationRuleConfig> existingRules = entity.getRules().stream()
                .collect(Collectors.toMap(ModerationRuleConfig::getRuleType, r -> r));

        entity.getRules().clear();

        for (ModerationRuleDto ruleDto : dto.getRules()) {
            ModerationRuleConfig rule = existingRules.getOrDefault(
                    ruleDto.getId(),
                    ModerationRuleConfig.builder().config(entity).build()
            );
            rule.setConfig(entity);
            rule.setRuleType(ruleDto.getId());
            rule.setEnabled(ruleDto.isEnabled());
            rule.setAction(ruleDto.getAction());
            rule.setThreshold(ruleDto.getThreshold());
            rule.setTimeoutMinutes(ruleDto.getTimeoutMinutes());
            entity.getRules().add(rule);
        }
    }
}