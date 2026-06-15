package com.kat.backend.guild.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuildUserResponse {

    private String id;
    private String name;
    private String icon;
    private boolean owner;

    @JsonProperty("bot_joined")
    private boolean botJoined;

    @JsonProperty("can_manage")
    private boolean canManage;

    @JsonProperty("permissions")
    private String permissions;
}
