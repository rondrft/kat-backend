package com.kat.backend.guild.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailyJoinDto {

    private String date;
    private long count;
}
