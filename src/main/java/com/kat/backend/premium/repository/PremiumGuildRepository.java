package com.kat.backend.premium.repository;

import com.kat.backend.premium.entity.PremiumGuild;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PremiumGuildRepository extends JpaRepository<PremiumGuild, String> {
}