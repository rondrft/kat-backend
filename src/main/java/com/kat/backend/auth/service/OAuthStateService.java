package com.kat.backend.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthStateService {

    private static final String PREFIX = "oauth:state:";
    private static final Duration TTL = Duration.ofMinutes(10);

    private final StringRedisTemplate stringRedisTemplate;

    public String generate() {
        String state = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(PREFIX + state, "1", TTL);
        return state;
    }

    public boolean consumeAndValidate(String state) {
        if (state == null || state.isBlank()) return false;
        return Boolean.TRUE.equals(stringRedisTemplate.delete(PREFIX + state));
    }
}
