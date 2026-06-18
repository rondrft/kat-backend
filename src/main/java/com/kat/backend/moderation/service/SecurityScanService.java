package com.kat.backend.moderation.service;

import com.kat.backend.guild.dto.RecentMemberDto;
import com.kat.backend.guild.dto.RoleDto;
import com.kat.backend.guild.dto.TextChannelDto;
import com.kat.backend.guild.service.BotGuildService;
import com.kat.backend.guild.service.GuildService;
import com.kat.backend.moderation.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityScanService {

    private static final long PERMISSION_ADMINISTRATOR = 1L << 3;
    private static final long PERMISSION_MANAGE_GUILD = 1L << 5;
    private static final long PERMISSION_MANAGE_CHANNELS = 1L << 4;
    private static final long PERMISSION_MANAGE_ROLES = 1L << 28;
    private static final long PERMISSION_BAN_MEMBERS = 1L << 2;
    private static final long PERMISSION_KICK_MEMBERS = 1L << 1;
    private static final long PERMISSION_MANAGE_MESSAGES = 1L << 13;
    private static final long PERMISSION_MENTION_EVERYONE = 1L << 17;
    private static final long PERMISSION_MANAGE_WEBHOOKS = 1L << 29;
    private static final long PERMISSION_VIEW_AUDIT_LOG = 1L << 7;

    private static final int RECENT_MEMBER_LIMIT = 20;
    private static final int NEW_ACCOUNT_DAYS = 7;

    private final BotGuildService botGuildService;
    private final GuildService guildService;
    private final ModerationService moderationService;

    public SecurityScanResultDto scan(String guildId) {
        long start = System.nanoTime();
        log.info("Starting security scan for guild {}", guildId);

        List<RoleDto> roles = safeList(botGuildService.getSecurityScanRoles(guildId));
        List<TextChannelDto> channels = safeList(botGuildService.getTextChannels(guildId, Pageable.unpaged()).getContent());
        List<RecentMemberDto> recentMembers = safeList(guildService.getRecentMembers(guildId, PageRequest.of(0, RECENT_MEMBER_LIMIT)).getContent());
        ModerationConfigDto config = safeConfig(guildId);

        List<SecurityScanFindingDto> findings = new ArrayList<>();

        analyzeChannels(channels, findings);
        analyzeRoles(roles, findings);
        analyzeMembers(recentMembers, findings);
        analyzeModerationConfig(config, findings);

        if (findings.isEmpty()) {
            findings.add(SecurityScanFindingDto.builder()
                    .id(UUID.randomUUID().toString())
                    .category(SecurityScanFindingDto.SecurityScanCategory.GENERAL)
                    .severity(SecurityScanSeverity.SUCCESS)
                    .title("Server looks healthy")
                    .description("No critical or warning issues were detected during this scan.")
                    .build());
        }

        List<SecurityScanFindingDto> sorted = findings.stream()
                .sorted(Comparator
                        .comparing((SecurityScanFindingDto f) -> severityRank(f.getSeverity()))
                        .thenComparing(f -> f.getCategory().name()))
                .toList();

        SecurityScanSummaryDto summary = buildSummary(sorted);
        int score = calculateScore(summary);
        List<String> topRecommendations = buildTopRecommendations(sorted);

        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        log.info("Security scan for guild {} completed in {} ms — score {}, findings {}",
                guildId, elapsedMs, score, summary.getTotal());

        return SecurityScanResultDto.builder()
                .scannedAt(OffsetDateTime.now().toString())
                .score(score)
                .summary(summary)
                .findings(sorted)
                .topRecommendations(topRecommendations)
                .build();
    }

    private void analyzeChannels(List<TextChannelDto> channels, List<SecurityScanFindingDto> findings) {
        if (channels.isEmpty()) {
            findings.add(SecurityScanFindingDto.builder()
                    .id(UUID.randomUUID().toString())
                    .category(SecurityScanFindingDto.SecurityScanCategory.CHANNEL)
                    .severity(SecurityScanSeverity.WARNING)
                    .title("No text channels found")
                    .description("The bot could not read any text channels. Verify its permissions.")
                    .recommendation("Re-invite the bot with channel read permissions.")
                    .build());
            return;
        }

        long nsfwCount = channels.stream().filter(TextChannelDto::isNsfw).count();
        if (nsfwCount > 0) {
            findings.add(SecurityScanFindingDto.builder()
                    .id(UUID.randomUUID().toString())
                    .category(SecurityScanFindingDto.SecurityScanCategory.CHANNEL)
                    .severity(SecurityScanSeverity.INFO)
                    .title("NSFW channels detected")
                    .description(nsfwCount + " text channel(s) are marked as age-restricted.")
                    .recommendation("Make sure NSFW channels are properly age-gated and moderated.")
                    .build());
        }

        long unprotectedChannels = channels.stream()
                .filter(c -> c.getSlowmodeDelay() == 0)
                .count();
        if (unprotectedChannels > 0) {
            findings.add(SecurityScanFindingDto.builder()
                    .id(UUID.randomUUID().toString())
                    .category(SecurityScanFindingDto.SecurityScanCategory.CHANNEL)
                    .severity(SecurityScanSeverity.INFO)
                    .title("Channels without slowmode")
                    .description(unprotectedChannels + " channel(s) have no rate limit. This makes spam raids easier.")
                    .recommendation("Consider enabling slowmode in public channels during high-traffic events.")
                    .build());
        }
    }

    private void analyzeRoles(List<RoleDto> roles, List<SecurityScanFindingDto> findings) {
        if (roles.isEmpty()) {
            findings.add(SecurityScanFindingDto.builder()
                    .id(UUID.randomUUID().toString())
                    .category(SecurityScanFindingDto.SecurityScanCategory.ROLE)
                    .severity(SecurityScanSeverity.WARNING)
                    .title("No roles found")
                    .description("The bot could not read server roles. Verify its permissions.")
                    .recommendation("Re-invite the bot with role read permissions.")
                    .build());
            return;
        }

        List<RoleDto> adminRoles = roles.stream()
                .filter(r -> hasPermission(r, PERMISSION_ADMINISTRATOR))
                .toList();
        if (!adminRoles.isEmpty()) {
            for (RoleDto role : adminRoles) {
                findings.add(SecurityScanFindingDto.builder()
                        .id(UUID.randomUUID().toString())
                        .category(SecurityScanFindingDto.SecurityScanCategory.ROLE)
                        .severity(role.isManaged() ? SecurityScanSeverity.WARNING : SecurityScanSeverity.CRITICAL)
                        .title("Administrator permission granted")
                        .targetId(role.getId())
                        .targetName(role.getName())
                        .description("The role '" + role.getName() + "' has full Administrator access.")
                        .recommendation(role.isManaged()
                                ? "Review bot permissions and remove Administrator if not strictly required."
                                : "Only assign Administrator to trusted owner/admin roles.")
                        .build());
            }
        }

        List<RoleDto> dangerousRoles = roles.stream()
                .filter(r -> !hasPermission(r, PERMISSION_ADMINISTRATOR))
                .filter(r -> hasAnyPermission(r,
                        PERMISSION_MANAGE_GUILD,
                        PERMISSION_MANAGE_ROLES,
                        PERMISSION_MANAGE_CHANNELS,
                        PERMISSION_BAN_MEMBERS,
                        PERMISSION_KICK_MEMBERS,
                        PERMISSION_MANAGE_WEBHOOKS))
                .toList();
        for (RoleDto role : dangerousRoles) {
            findings.add(SecurityScanFindingDto.builder()
                    .id(UUID.randomUUID().toString())
                    .category(SecurityScanFindingDto.SecurityScanCategory.ROLE)
                    .severity(SecurityScanSeverity.WARNING)
                    .title("Elevated moderation permissions")
                    .targetId(role.getId())
                    .targetName(role.getName())
                    .description("The role '" + role.getName() + "' can manage server settings, members, or webhooks.")
                    .recommendation("Restrict this role to active, trusted moderators.")
                    .build());
        }

        List<RoleDto> mentionableEveryoneRoles = roles.stream()
                .filter(RoleDto::isMentionable)
                .filter(r -> !r.isManaged())
                .toList();
        if (!mentionableEveryoneRoles.isEmpty()) {
            findings.add(SecurityScanFindingDto.builder()
                    .id(UUID.randomUUID().toString())
                    .category(SecurityScanFindingDto.SecurityScanCategory.ROLE)
                    .severity(SecurityScanSeverity.INFO)
                    .title("Mentionable roles")
                    .description(mentionableEveryoneRoles.size() + " role(s) can be mentioned by anyone.")
                    .recommendation("Disable mentionable on non-essential roles to reduce ping abuse.")
                    .build());
        }

        long hoistedRoles = roles.stream().filter(RoleDto::isHoisted).count();
        if (hoistedRoles > 10) {
            findings.add(SecurityScanFindingDto.builder()
                    .id(UUID.randomUUID().toString())
                    .category(SecurityScanFindingDto.SecurityScanCategory.ROLE)
                    .severity(SecurityScanSeverity.INFO)
                    .title("Many hoisted roles")
                    .description(hoistedRoles + " roles are displayed separately in the member list.")
                    .recommendation("Too many hoisted roles clutter the sidebar; consider consolidating.")
                    .build());
        }
    }

    private void analyzeMembers(List<RecentMemberDto> members, List<SecurityScanFindingDto> findings) {
        if (members.isEmpty()) {
            return;
        }

        long criticalCount = members.stream()
                .filter(m -> "red".equalsIgnoreCase(m.getAlertLevel()))
                .count();
        long warningCount = members.stream()
                .filter(m -> "yellow".equalsIgnoreCase(m.getAlertLevel()))
                .count();

        if (criticalCount > 0) {
            findings.add(SecurityScanFindingDto.builder()
                    .id(UUID.randomUUID().toString())
                    .category(SecurityScanFindingDto.SecurityScanCategory.MEMBER)
                    .severity(SecurityScanSeverity.CRITICAL)
                    .title("High-risk recent members")
                    .description(criticalCount + " of the last " + members.size() + " joiners triggered critical alerts (new accounts or unverified bots).")
                    .recommendation("Review recent joins and consider enabling join-raid or account-age protection.")
                    .build());
        } else if (warningCount > 0) {
            findings.add(SecurityScanFindingDto.builder()
                    .id(UUID.randomUUID().toString())
                    .category(SecurityScanFindingDto.SecurityScanCategory.MEMBER)
                    .severity(SecurityScanSeverity.WARNING)
                    .title("Suspicious recent members")
                    .description(warningCount + " of the last " + members.size() + " joiners look suspicious (no avatar or odd username).")
                    .recommendation("Keep an eye on these accounts or enable stricter verification.")
                    .build());
        } else {
            findings.add(SecurityScanFindingDto.builder()
                    .id(UUID.randomUUID().toString())
                    .category(SecurityScanFindingDto.SecurityScanCategory.MEMBER)
                    .severity(SecurityScanSeverity.SUCCESS)
                    .title("Recent members look clean")
                    .description("The last " + members.size() + " joiners did not trigger any major alerts.")
                    .build());
        }
    }

    private void analyzeModerationConfig(ModerationConfigDto config, List<SecurityScanFindingDto> findings) {
        if (config == null) {
            findings.add(SecurityScanFindingDto.builder()
                    .id(UUID.randomUUID().toString())
                    .category(SecurityScanFindingDto.SecurityScanCategory.GENERAL)
                    .severity(SecurityScanSeverity.WARNING)
                    .title("Moderation config unavailable")
                    .description("The bot could not load the active moderation configuration.")
                    .recommendation("Check that the bot is in the server and has the required permissions.")
                    .build());
            return;
        }

        if (!Boolean.TRUE.equals(config.isEnabled())) {
            findings.add(SecurityScanFindingDto.builder()
                    .id(UUID.randomUUID().toString())
                    .category(SecurityScanFindingDto.SecurityScanCategory.GENERAL)
                    .severity(SecurityScanSeverity.CRITICAL)
                    .title("Auto-moderation is disabled")
                    .description("Automatic moderation is currently turned off.")
                    .recommendation("Enable auto-moderation and configure at least the free protection rules.")
                    .build());
        }

        long activeRules = Optional.ofNullable(config.getRules())
                .orElse(List.of())
                .stream()
                .filter(ModerationRuleDto::isEnabled)
                .count();
        if (activeRules == 0) {
            findings.add(SecurityScanFindingDto.builder()
                    .id(UUID.randomUUID().toString())
                    .category(SecurityScanFindingDto.SecurityScanCategory.GENERAL)
                    .severity(SecurityScanSeverity.WARNING)
                    .title("No protection rules enabled")
                    .description("Auto-moderation is on but no rules are active.")
                    .recommendation("Enable relevant rules such as spam, links, invites, and phishing.")
                    .build());
        } else {
            findings.add(SecurityScanFindingDto.builder()
                    .id(UUID.randomUUID().toString())
                    .category(SecurityScanFindingDto.SecurityScanCategory.GENERAL)
                    .severity(SecurityScanSeverity.SUCCESS)
                    .title("Protection rules active")
                    .description(activeRules + " moderation rule(s) are enabled.")
                    .build());
        }
    }

    private SecurityScanSummaryDto buildSummary(List<SecurityScanFindingDto> findings) {
        AtomicInteger critical = new AtomicInteger();
        AtomicInteger warning = new AtomicInteger();
        AtomicInteger info = new AtomicInteger();
        AtomicInteger success = new AtomicInteger();

        findings.forEach(f -> {
            switch (f.getSeverity()) {
                case CRITICAL -> critical.incrementAndGet();
                case WARNING -> warning.incrementAndGet();
                case INFO -> info.incrementAndGet();
                case SUCCESS -> success.incrementAndGet();
            }
        });

        return SecurityScanSummaryDto.builder()
                .total(findings.size())
                .critical(critical.get())
                .warning(warning.get())
                .info(info.get())
                .success(success.get())
                .build();
    }

    private int calculateScore(SecurityScanSummaryDto summary) {
        int score = 100;
        score -= summary.getCritical() * 15;
        score -= summary.getWarning() * 8;
        score -= summary.getInfo() * 2;
        score += summary.getSuccess() * 2;
        return Math.max(0, Math.min(100, score));
    }

    private List<String> buildTopRecommendations(List<SecurityScanFindingDto> findings) {
        return findings.stream()
                .filter(f -> f.getSeverity() == SecurityScanSeverity.CRITICAL || f.getSeverity() == SecurityScanSeverity.WARNING)
                .filter(f -> f.getRecommendation() != null && !f.getRecommendation().isBlank())
                .map(SecurityScanFindingDto::getRecommendation)
                .distinct()
                .limit(5)
                .collect(Collectors.toList());
    }

    private int severityRank(SecurityScanSeverity severity) {
        return switch (severity) {
            case CRITICAL -> 0;
            case WARNING -> 1;
            case INFO -> 2;
            case SUCCESS -> 3;
        };
    }

    private boolean hasPermission(RoleDto role, long permission) {
        return role != null && (role.getPermissions() & permission) == permission;
    }

    private boolean hasAnyPermission(RoleDto role, long... permissions) {
        if (role == null) return false;
        for (long permission : permissions) {
            if ((role.getPermissions() & permission) == permission) {
                return true;
            }
        }
        return false;
    }

    private <T> List<T> safeList(List<T> list) {
        return list != null ? list : List.of();
    }

    private ModerationConfigDto safeConfig(String guildId) {
        try {
            return moderationService.getConfig(guildId);
        } catch (Exception e) {
            log.warn("Could not load moderation config for security scan in guild {}: {}", guildId, e.getMessage());
            return null;
        }
    }
}
