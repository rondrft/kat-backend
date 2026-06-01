package com.kat.backend.guild.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.Map;

@Getter
@Builder
public class GuildStatsDto {
    private int totalMembers;
    private int totalTextChannels;
    private int totalVoiceChannels;
    private int totalCategories;
    private long totalRoles;
    private int totalEmojis;
    private String serverCreatedAt;
    private int boostLevel;
    private int boostCount;
    private long activeCustomRoles;
    private Map<String, Boolean> activeModules;
}