package com.kat.backend.user.service;

import com.kat.backend.discord.DiscordUserResponse;
import com.kat.backend.user.dto.UserResponse;
import com.kat.backend.user.entity.User;
import com.kat.backend.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getByDiscordId(String discordId) {
        User user = userRepository.findByDiscordId(discordId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + discordId));

        return UserResponse.builder()
                .id(user.getId())
                .discordId(user.getDiscordId())
                .username(user.getUsername())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .build();
    }

    @Transactional
    public User findOrCreate(DiscordUserResponse discordUser, String accessToken) {

        return userRepository.findByDiscordId(discordUser.getId())
                .map(existing -> updateUser(existing, discordUser, accessToken))
                .orElseGet(() -> createUser(discordUser, accessToken));
    }

    private User updateUser(User user, DiscordUserResponse discordUser, String accessToken) {
        user.setUsername(discordUser.getUsername());
        user.setAvatar(discordUser.getAvatar());
        user.setEmail(discordUser.getEmail());
        user.setDiscordAccessToken(accessToken);
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }

    private User createUser(DiscordUserResponse discordUser, String accessToken) {
        User user = User.builder()
                .discordId(discordUser.getId())
                .username(discordUser.getUsername())
                .avatar(discordUser.getAvatar())
                .email(discordUser.getEmail())
                .emailVerified(discordUser.getVerified())
                .discordAccessToken(accessToken)
                .createdAt(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .build();
        return userRepository.save(user);
    }

}
