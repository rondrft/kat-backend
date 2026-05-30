package com.kat.backend.guild.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RecentMemberDto {

    private String id;
    private String username;
    private String avatar;
    private String joinedAt;
    private String accountCreatedAt;
    private boolean bot;
    private boolean verifiedBot;
    private String alertLevel;
    private List<String> alertReasons;
}