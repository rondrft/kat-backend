package com.kat.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerGuard {

    @Value("${bot.owner-id}")
    private String ownerId;

    public boolean isOwner(String discordId) {
        return ownerId != null && ownerId.equals(discordId);
    }
}