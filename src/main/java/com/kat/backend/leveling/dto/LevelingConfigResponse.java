package com.kat.backend.leveling.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LevelingConfigResponse {
    private String guildId;
    private boolean enabled;
}
