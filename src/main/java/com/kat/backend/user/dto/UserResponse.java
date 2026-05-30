package com.kat.backend.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class UserResponse {
    private UUID id;
    private String discordId;
    private String username;
    private String avatar;
    private String email;
}