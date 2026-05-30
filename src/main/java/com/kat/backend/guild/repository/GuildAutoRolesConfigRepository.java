package com.kat.backend.guild.repository;

import com.kat.backend.guild.entity.GuildAutoRolesConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuildAutoRolesConfigRepository
        extends JpaRepository<GuildAutoRolesConfig, String> {
}