package com.kat.backend.guild.repository;

import com.kat.backend.guild.entity.GuildConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildConfigRepository extends JpaRepository<GuildConfig, String> {
}
