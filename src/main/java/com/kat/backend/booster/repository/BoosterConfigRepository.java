package com.kat.backend.booster.repository;

import com.kat.backend.booster.entity.BoosterConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoosterConfigRepository extends JpaRepository<BoosterConfig, String> {
}