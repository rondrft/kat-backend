package com.kat.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class JwtDenylistService {

    private static final String PREFIX = "jwt:denylist:";

    private final StringRedisTemplate stringRedisTemplate;

    public void revoke(String jti, long ttlSeconds) {
        if (ttlSeconds > 0) {
            stringRedisTemplate.opsForValue().set(PREFIX + jti, "1", Duration.ofSeconds(ttlSeconds));
        }
    }

    public boolean isDenylisted(String jti) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(PREFIX + jti));
    }
}
