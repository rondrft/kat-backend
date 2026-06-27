package com.kat.backend.guild.repository;

import com.kat.backend.guild.entity.GuildMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface GuildMemberRepository extends JpaRepository<GuildMember, String> {

    @Query("SELECT g FROM GuildMember g WHERE g.guildId = :guildId ORDER BY g.joinedAt DESC")
    Page<GuildMember> findRecentByGuildId(@Param("guildId") String guildId,
                                          Pageable pageable);

    @Query(value = """
            SELECT to_char(joined_at, 'YYYY-MM-DD') AS day, COUNT(*) AS cnt
            FROM guild_members
            WHERE guild_id = :guildId AND joined_at >= :since
            GROUP BY to_char(joined_at, 'YYYY-MM-DD')
            """, nativeQuery = true)
    List<Object[]> countJoinsByDay(@Param("guildId") String guildId,
                                   @Param("since") LocalDateTime since);
}