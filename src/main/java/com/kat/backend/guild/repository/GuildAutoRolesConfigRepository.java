package com.kat.backend.guild.repository;

import com.kat.backend.guild.entity.GuildAutoRolesConfig;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuildAutoRolesConfigRepository
        extends JpaRepository<GuildAutoRolesConfig, String> {

    @EntityGraph(attributePaths = {"joinRoleIds", "boostRoleIds", "reactionMappings"})
    Optional<GuildAutoRolesConfig> findWithCollectionsByGuildId(String guildId);
}