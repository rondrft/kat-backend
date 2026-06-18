package com.kat.backend.moderation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SecurityScanSummaryDto {

    private int total;
    private int critical;
    private int warning;
    private int info;
    private int success;
}
