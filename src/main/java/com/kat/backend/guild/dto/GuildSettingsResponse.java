package com.kat.backend.guild.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GuildSettingsResponse {
    private String guildId;
    private String prefix;
    private String locale;
}
