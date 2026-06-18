package com.kat.backend.moderation.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SecurityScanResultDto {

    private String scannedAt;
    private int score;
    private SecurityScanSummaryDto summary;
    private List<SecurityScanFindingDto> findings;
    private List<String> topRecommendations;
}
