package com.kat.backend.guild.dto;

import lombok.Data;

@Data
public class GuildSettingsRequest {
    private String prefix;
    private String locale;
}
