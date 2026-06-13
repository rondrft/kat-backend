package com.kat.backend.moderation.repository;

import com.kat.backend.moderation.entity.ModerationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface ModerationLogRepository extends JpaRepository<ModerationLog, UUID> {

    @Query("SELECT l FROM ModerationLog l WHERE l.guildId = :guildId ORDER BY l.createdAt DESC")
    Page<ModerationLog> findByGuildId(@Param("guildId") String guildId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM ModerationLog l WHERE l.createdAt < :cutoff")
    int deleteOlderThan(@Param("cutoff") Instant cutoff);

    @Modifying
    @Query(value = "DELETE FROM moderation_logs WHERE ctid IN (SELECT ctid FROM moderation_logs WHERE created_at < :cutoff LIMIT :limit)", nativeQuery = true)
    int deleteOlderThan(@Param("cutoff") Instant cutoff, @Param("limit") int limit);
}