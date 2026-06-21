package com.kat.backend.works.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WorkConfigResponse {
    private String guildId;
    private boolean enabled;
    private List<String> allowedChannelIds;
}
