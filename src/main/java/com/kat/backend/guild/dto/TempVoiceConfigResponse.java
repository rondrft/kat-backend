package com.kat.backend.guild.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TempVoiceConfigResponse {
    private String guildId;
    private boolean enabled;
    private String categoryId;
    private String hubChannelId;
    private String channelNameTemplate;
    private int userLimit;
    private int deleteDelaySeconds;
    private boolean lockedToOwner;
}