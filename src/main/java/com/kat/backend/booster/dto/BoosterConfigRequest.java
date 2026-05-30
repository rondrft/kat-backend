package com.kat.backend.booster.dto;

import lombok.Data;

@Data
public class BoosterConfigRequest {
    private boolean enabled;
    private int requiredBoosts;
    private boolean allowInvites;
}