package com.kat.backend.guild.repository;

import com.kat.backend.guild.entity.GuildTempVoiceConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuildTempVoiceConfigRepository extends JpaRepository<GuildTempVoiceConfig, String> {
}