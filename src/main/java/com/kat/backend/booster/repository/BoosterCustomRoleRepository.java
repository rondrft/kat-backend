package com.kat.backend.booster.repository;

import com.kat.backend.booster.entity.BoosterCustomRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoosterCustomRoleRepository extends JpaRepository<BoosterCustomRole, UUID> {

    Optional<BoosterCustomRole> findByGuildIdAndOwnerDiscordId(String guildId, String ownerDiscordId);

    List<BoosterCustomRole> findAllByGuildId(String guildId);

    long countByGuildId(String guildId);
}