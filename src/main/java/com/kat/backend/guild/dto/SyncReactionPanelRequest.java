package com.kat.backend.guild.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SyncReactionPanelRequest {
    private String channelId;
    private String messageContent;
    private String messageId;
    private List<ReactionRoleMappingDto> mappings;
    private boolean useEmbed;
    private String embedTitle;
    private String embedColor;
}