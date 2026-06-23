package com.kat.backend.auth.controller;

import com.kat.backend.auth.dto.AuthResponse;
import com.kat.backend.auth.dto.DiscordCallbackRequest;
import com.kat.backend.auth.service.AuthService;
import com.kat.backend.auth.service.OAuthStateService;
import com.kat.backend.common.ApiResponse;
import com.kat.backend.security.JwtDenylistService;
import com.kat.backend.security.JwtUtil;
import com.kat.backend.user.dto.UserResponse;
import com.kat.backend.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final JwtDenylistService jwtDenylistService;
    private final OAuthStateService oAuthStateService;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    @Value("${app.cookie-secure:false}")
    private boolean cookieSecure;

    @GetMapping("/oauth/state")
    public ResponseEntity<ApiResponse<Map<String, String>>> generateOAuthState() {
        return ResponseEntity.ok(ApiResponse.ok(Map.of("state", oAuthStateService.generate())));
    }

    @PostMapping("/discord/callback")
    public ResponseEntity<ApiResponse<AuthResponse>> discordCallback(
            @Valid @RequestBody DiscordCallbackRequest request,
            HttpServletResponse httpResponse) {

        if (!oAuthStateService.consumeAndValidate(request.getState())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired OAuth state");
        }

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
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest httpRequest,
                                                     HttpServletResponse httpResponse) {
        String token = extractTokenFromCookies(httpRequest);
        if (token != null && jwtUtil.validateToken(token)) {
            jwtDenylistService.revoke(jwtUtil.getJtiFromToken(token), jwtUtil.getRemainingTtlSeconds(token));
        }

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

    private String extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("kat-access-token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
