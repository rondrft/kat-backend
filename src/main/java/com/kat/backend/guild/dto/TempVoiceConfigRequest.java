package com.kat.backend.guild.dto;

import lombok.Data;

@Data
public class TempVoiceConfigRequest {
    private boolean enabled;
    private String categoryId;
    private String channelNameTemplate;
    private int userLimit;
    private int deleteDelaySeconds;
    private boolean lockedToOwner;
}