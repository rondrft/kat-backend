package com.kat.backend.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    @JsonIgnore
    private String token;
    private String discordId;
    private String username;
    private String avatar;

}
