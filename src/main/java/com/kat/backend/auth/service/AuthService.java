package com.kat.backend.auth.service;

import com.kat.backend.auth.dto.AuthResponse;
import com.kat.backend.discord.DiscordClient;
import com.kat.backend.discord.DiscordTokenResponse;
import com.kat.backend.discord.DiscordUserResponse;
import com.kat.backend.exception.DiscordAuthException;
import com.kat.backend.security.JwtUtil;
import com.kat.backend.user.entity.User;
import com.kat.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final DiscordClient discordClient;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthResponse handleDiscordCallback(String code) {

        DiscordTokenResponse tokenResponse = discordClient.exchangeCodeForToken(code);

        if (tokenResponse == null || tokenResponse.getAccessToken() == null) {
            throw new DiscordAuthException("Could not get Discord token");
        }

        DiscordUserResponse discordUser = discordClient.getUserInfo(tokenResponse.getAccessToken());

        if (discordUser == null || discordUser.getId() == null) {
            throw new DiscordAuthException("Could not get Discord user");
        }

        User user = userService.findOrCreate(discordUser, tokenResponse.getAccessToken());

        String token = jwtUtil.generateToken(user.getDiscordId(), user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .discordId(user.getDiscordId())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .build();
    }
}