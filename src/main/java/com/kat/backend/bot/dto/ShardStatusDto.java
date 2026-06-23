package com.kat.backend.bot.dto;

import lombok.Data;

@Data
public class ShardStatusDto {
    private int shardId;
    private int totalShards;
    private String status;
    private long pingMs;
    private int guilds;
    private int users;
}
