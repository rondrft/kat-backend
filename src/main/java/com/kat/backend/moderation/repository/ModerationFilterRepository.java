package com.kat.backend.moderation.repository;

import com.kat.backend.moderation.entity.ModerationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModerationFilterRepository extends JpaRepository<ModerationFilter, UUID> {
    List<ModerationFilter> findByGuildId(String guildId);
}
