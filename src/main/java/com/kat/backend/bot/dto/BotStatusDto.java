package com.kat.backend.bot.dto;

import lombok.Data;

import java.util.List;

@Data
public class BotStatusDto {
    private int totalShards;
    private int connectedShards;
    private int totalGuilds;
    private int totalUsers;
    private List<ShardStatusDto> shards;
}
