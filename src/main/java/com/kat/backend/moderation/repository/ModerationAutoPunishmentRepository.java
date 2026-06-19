package com.kat.backend.moderation.repository;

import com.kat.backend.moderation.entity.ModerationAutoPunishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModerationAutoPunishmentRepository extends JpaRepository<ModerationAutoPunishment, UUID> {
    List<ModerationAutoPunishment> findByGuildId(String guildId);
    void deleteByGuildId(String guildId);
}
