package com.kat.backend.security;

import com.kat.backend.guild.service.AdminPermissionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class GuildAdminAspect {

    private final AdminPermissionService adminPermissionService;

    @Around("@annotation(GuildAdmin)")
    public Object checkGuildAdmin(ProceedingJoinPoint joinPoint) throws Throwable {
        String discordId = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();

        Map<?, ?> pathVariables = (Map<?, ?>) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String guildId = pathVariables != null ? (String) pathVariables.get("guildId") : null;

        if (guildId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing guildId");
        }

        if (!adminPermissionService.isAdmin(guildId, discordId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You don't have admin permissions in this guild");
        }

        return joinPoint.proceed();
    }
}