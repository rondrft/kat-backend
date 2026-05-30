package com.kat.backend.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    private String token;
    private String discordId;
    private String username;
    private String avatar;

}
