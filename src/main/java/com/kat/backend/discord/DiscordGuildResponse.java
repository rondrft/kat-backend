package com.kat.backend.discord;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DiscordGuildResponse {

    private String id;
    private String name;
    private String icon;
    private boolean owner;
    private String permissions;
}