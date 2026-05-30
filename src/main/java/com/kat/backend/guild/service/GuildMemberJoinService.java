package com.kat.backend.guild.service;

import com.kat.backend.guild.entity.GuildMember;
import com.kat.backend.guild.repository.GuildMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuildMemberJoinService {

    private final GuildMemberRepository guildMemberRepository;

    @Transactional
    public void recordJoin(String guildId, String discordUserId, String username, String avatar) {
        GuildMember member = new GuildMember();
        member.setId(UUID.randomUUID().toString());
        member.setGuildId(guildId);
        member.setDiscordUserId(discordUserId);
        member.setUsername(username != null ? username : "user");
        member.setAvatar(avatar);
        member.setJoinedAt(LocalDateTime.now());

        guildMemberRepository.save(member);
        log.info("Recorded guild join: guild={} user={} ({})", guildId, discordUserId, username);
    }
}
