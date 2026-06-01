package com.kat.backend.moderation.repository;

import com.kat.backend.moderation.entity.ModerationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModerationConfigRepository extends JpaRepository<ModerationConfig, String> {
}