package com.kat.backend.booster.dto;

import com.kat.backend.booster.entity.BoosterConfig;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoosterConfigResponse {

    private String guildId;
    private boolean enabled;
    private int requiredBoosts;
    private boolean allowInvites;

    public static BoosterConfigResponse from(BoosterConfig entity) {
        return BoosterConfigResponse.builder()
                .guildId(entity.getGuildId())
                .enabled(entity.isEnabled())
                .requiredBoosts(entity.getRequiredBoosts())
                .allowInvites(entity.isAllowInvites())
                .build();
    }
}