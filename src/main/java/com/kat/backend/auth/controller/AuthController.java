package com.kat.backend.auth.controller;

import com.kat.backend.auth.dto.AuthResponse;
import com.kat.backend.auth.dto.DiscordCallbackRequest;
import com.kat.backend.auth.service.AuthService;
import com.kat.backend.common.ApiResponse;
import com.kat.backend.user.dto.UserResponse;
import com.kat.backend.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    @Value("${app.cookie-secure:false}")
    private boolean cookieSecure;

    @PostMapping("/discord/callback")
    public ResponseEntity<ApiResponse<AuthResponse>> discordCallback(
            @Valid @RequestBody DiscordCallbackRequest request,
            HttpServletResponse httpResponse) {

        AuthResponse response = authService.handleDiscordCallback(request.getCode());

        ResponseCookie cookie = ResponseCookie.from("kat-access-token", response.getToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(jwtExpiration / 1000)
                .sameSite("Strict")
                .build();
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(
            @AuthenticationPrincipal String discordId) {

        UserResponse user = userService.getByDiscordId(discordId);
        return ResponseEntity.ok(ApiResponse.ok(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletResponse httpResponse) {
        ResponseCookie cookie = ResponseCookie.from("kat-access-token", "")
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
