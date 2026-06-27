package com.kat.backend.security;

import com.kat.backend.guild.client.GuildPermissionClient;
import com.kat.backend.guild.service.AdminPermissionService;
import com.kat.backend.guild.service.DashboardAccessService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;
import java.util.Map;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class GuildAdminAspect {

    private final AdminPermissionService adminPermissionService;
    private final DashboardAccessService dashboardAccessService;
    private final GuildPermissionClient guildPermissionClient;

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

        verifyAccess(guildId, discordId);

        return joinPoint.proceed();
    }

    private void verifyAccess(String guildId, String discordId) {
        try {
            if (adminPermissionService.isAdmin(guildId, discordId)) return;
            if (dashboardAccessService.hasUserAccess(guildId, discordId)) return;

            List<String> allowedRoles = dashboardAccessService.getAllowedRoleIds(guildId);
            if (!allowedRoles.isEmpty() && guildPermissionClient.hasAnyRole(guildId, discordId, allowedRoles)) return;
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Bot is temporarily unavailable");
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "You don't have access to this guild's dashboard");
    }
}
