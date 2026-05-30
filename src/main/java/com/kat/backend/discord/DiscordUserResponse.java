package com.kat.backend.discord;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DiscordUserResponse {

    private String id;
    private String username;
    private String avatar;
    private String email;

    @JsonProperty("verified")
    private Boolean verified;
}