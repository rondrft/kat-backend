package com.kat.backend.welcome.repository;

import com.kat.backend.welcome.entity.WelcomeConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WelcomeConfigRepository extends JpaRepository<WelcomeConfig, String> {
}