package com.kat.backend.works.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WorkConfigResponse {
    private String guildId;
    private boolean enabled;
}
