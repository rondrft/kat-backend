package com.kat.backend.guild.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MonthlyJoinStatsDto {

    private long total;
    private List<DailyJoinDto> days;
}
