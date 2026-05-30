package com.kat.backend.auth.controller;

import com.kat.backend.auth.dto.AuthResponse;
import com.kat.backend.auth.dto.DiscordCallbackRequest;
import com.kat.backend.auth.service.AuthService;
import com.kat.backend.common.ApiResponse;
import com.kat.backend.user.dto.UserResponse;
import com.kat.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/discord/callback")
    public ResponseEntity<ApiResponse<AuthResponse>> discordCallback(
            @Valid @RequestBody DiscordCallbackRequest request) {

        AuthResponse response = authService.handleDiscordCallback(request.getCode());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(
            @AuthenticationPrincipal String discordId) {

        UserResponse user = userService.getByDiscordId(discordId);
        return ResponseEntity.ok(ApiResponse.ok(user));
    }
}
