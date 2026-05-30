package com.kat.backend.guild.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberJoinStatDto {
    private String date;
    private Long count;
}