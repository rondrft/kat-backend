package com.kat.backend.guild.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter @Setter @NoArgsConstructor
public class AutoRolesConfigResponse {
    private boolean joinEnabled;
    private List<String> joinRoleIds;
    private boolean boostEnabled;
    private List<String> boostRoleIds;
    private boolean reactionEnabled;
    private String reactionChannelId;
    private String reactionMessageContent;
    private String reactionMessageId;
    private List<ReactionRoleMappingDto> reactionMappings;
    private boolean reactionUseEmbed;
    private String reactionEmbedTitle;
    private String reactionEmbedColor;
}