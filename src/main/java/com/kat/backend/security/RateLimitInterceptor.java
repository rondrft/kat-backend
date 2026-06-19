package com.kat.backend.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RateLimitInterceptor.class);

    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(10))
            .maximumSize(50_000)
            .build();

    private Bucket resolveBucket(String ip, boolean isAuth) {
        return buckets.get(ip + (isAuth ? ":auth" : ":api"), k -> createBucket(isAuth));
    }

    private Bucket createBucket(boolean isAuth) {
        Bandwidth limit = isAuth
                ? Bandwidth.builder().capacity(10).refillGreedy(10, Duration.ofMinutes(1)).build()
                : Bandwidth.builder().capacity(60).refillGreedy(60, Duration.ofMinutes(1)).build();
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String path = request.getRequestURI();
        if (path.startsWith("/internal/") || path.startsWith("/actuator/")) {
            return true;
        }

        String ip = resolveClientIp(request);
        boolean isAuth = path.startsWith("/auth/");
        Bucket bucket = resolveBucket(ip, isAuth);

        if (bucket.tryConsume(1)) {
            return true;
        }

        log.warn("Rate limit exceeded for IP {} on path {}", ip, path);
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"success\":false,\"message\":\"Too many requests. Please slow down.\"}");
        return false;
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}