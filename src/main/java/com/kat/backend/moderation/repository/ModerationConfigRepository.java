package com.kat.backend.moderation.repository;

import com.kat.backend.moderation.entity.ModerationConfig;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModerationConfigRepository extends JpaRepository<ModerationConfig, String> {

    @EntityGraph(attributePaths = "rules")
    Optional<ModerationConfig> findWithRulesByGuildId(String guildId);
}