package com.kat.backend.guild.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActionsConfigResponse {
    private String guildId;
    private boolean enabled;
}
