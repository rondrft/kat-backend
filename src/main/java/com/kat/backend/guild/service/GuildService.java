package com.kat.backend.guild.service;

import com.kat.backend.discord.DiscordClient;
import com.kat.backend.discord.DiscordGuildResponse;
import com.kat.backend.guild.dto.DailyJoinDto;
import com.kat.backend.guild.dto.MemberJoinStatDto;
import com.kat.backend.guild.dto.MonthlyJoinStatsDto;
import com.kat.backend.guild.dto.RecentMemberDto;
import com.kat.backend.guild.repository.GuildMemberRepository;
import com.kat.backend.user.entity.User;
import com.kat.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuildService {

    private final DiscordClient discordClient;
    private final UserRepository userRepository;
    private final GuildMemberRepository guildMemberRepository;

    public MonthlyJoinStatsDto getMonthlyJoinStats(String guildId) {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(29);
        LocalDateTime since = start.atStartOfDay();

        Map<LocalDate, Long> countsByDay = guildMemberRepository
                .findByGuildIdAndJoinedAtGreaterThanEqual(guildId, since)
                .stream()
                .collect(Collectors.groupingBy(
                        m -> m.getJoinedAt().toLocalDate(),
                        Collectors.counting()));

        List<DailyJoinDto> days = new ArrayList<>();
        long total = 0;
        for (LocalDate day = start; !day.isAfter(today); day = day.plusDays(1)) {
            long count = countsByDay.getOrDefault(day, 0L);
            total += count;
            days.add(DailyJoinDto.builder()
                    .date(day.toString())
                    .count(count)
                    .build());
        }

        return MonthlyJoinStatsDto.builder()
                .total(total)
                .days(days)
                .build();
    }

    public List<RecentMemberDto> getRecentMembers(String guildId, int limit) {
        return guildMemberRepository.findRecentByGuildId(guildId, limit)
                .stream()
                .map(m -> {
                    List<String> reasons = new ArrayList<>();
                    String level;

                    boolean isBot = Boolean.TRUE.equals(m.getBot());
                    boolean isVerifiedBot = Boolean.TRUE.equals(m.getVerifiedBot());

                    if (isBot && !isVerifiedBot) {
                        reasons.add("Unverified bot");
                    }
                    if (m.getAccountCreatedAt() != null) {
                        long ageDays = java.time.temporal.ChronoUnit.DAYS
                                .between(m.getAccountCreatedAt(), LocalDateTime.now());
                        if (ageDays < 7) reasons.add("Account created " + ageDays + " days ago");
                    }

                    if (!reasons.isEmpty()) {
                        level = "red";
                    } else {
                        if (m.getAvatar() == null) reasons.add("No avatar");
                        if (m.getUsername() != null && m.getUsername().matches("[0-9_\\.]+")) {
                            reasons.add("Suspicious username");
                        }
                        level = reasons.isEmpty() ? "green" : "yellow";
                    }

                    return RecentMemberDto.builder()
                            .id(m.getDiscordUserId())
                            .username(m.getUsername())
                            .avatar(m.getAvatar())
                            .joinedAt(m.getJoinedAt().toString())
                            .accountCreatedAt(m.getAccountCreatedAt() != null
                                    ? m.getAccountCreatedAt().toString() : null)
                            .bot(isBot)
                            .verifiedBot(isVerifiedBot)
                            .alertLevel(level)
                            .alertReasons(reasons)
                            .build();
                })
                .toList();
    }

    public List<DiscordGuildResponse> getUserGuilds(String discordId) {
        User user = userRepository.findByDiscordId(discordId)
                .orElseThrow(() -> new RuntimeException("User not found: " + discordId));

        return discordClient.getUserGuilds(user.getDiscordAccessToken());
    }

    public List<MemberJoinStatDto> getMemberJoinStats(String guildId, int days) {
        int windowDays = Math.max(1, days);
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(windowDays - 1L);
        LocalDateTime since = start.atStartOfDay();

        Map<LocalDate, Long> countsByDay = guildMemberRepository
                .findByGuildIdAndJoinedAtGreaterThanEqual(guildId, since)
                .stream()
                .collect(Collectors.groupingBy(
                        m -> m.getJoinedAt().toLocalDate(),
                        Collectors.counting()));

        List<MemberJoinStatDto> stats = new ArrayList<>();
        for (LocalDate day = start; !day.isAfter(today); day = day.plusDays(1)) {
            stats.add(MemberJoinStatDto.builder()
                    .date(day.toString())
                    .count(countsByDay.getOrDefault(day, 0L))
                    .build());
        }
        return stats;
    }
}