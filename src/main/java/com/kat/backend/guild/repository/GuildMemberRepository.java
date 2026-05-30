package com.kat.backend.guild.repository;

import com.kat.backend.guild.entity.GuildMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface GuildMemberRepository extends JpaRepository<GuildMember, String> {

    @Query("SELECT g FROM GuildMember g WHERE g.guildId = :guildId ORDER BY g.joinedAt DESC LIMIT :limit")
    List<GuildMember> findRecentByGuildId(@Param("guildId") String guildId,
                                          @Param("limit") int limit);

    List<GuildMember> findByGuildIdAndJoinedAtGreaterThanEqual(String guildId, LocalDateTime since);
}