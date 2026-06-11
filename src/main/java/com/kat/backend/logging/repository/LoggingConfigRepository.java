package com.kat.backend.logging.repository;

import com.kat.backend.logging.entity.LoggingConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoggingConfigRepository extends JpaRepository<LoggingConfig, String> {
}