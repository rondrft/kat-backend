package com.kat.backend.logging.repository;

import com.kat.backend.logging.entity.LoggingConfig;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoggingConfigRepository extends JpaRepository<LoggingConfig, String> {

    @EntityGraph(attributePaths = "entries")
    Optional<LoggingConfig> findWithEntriesByGuildId(String guildId);
}