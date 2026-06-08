package com.kat.backend.booster.repository;

import com.kat.backend.booster.entity.BoosterCustomRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BoosterCustomRoleRepository extends JpaRepository<BoosterCustomRole, UUID> {

    Optional<BoosterCustomRole> findByGuildIdAndOwnerDiscordId(String guildId, String ownerDiscordId);

    Page<BoosterCustomRole> findAllByGuildId(String guildId, Pageable pageable);

    long countByGuildId(String guildId);
}