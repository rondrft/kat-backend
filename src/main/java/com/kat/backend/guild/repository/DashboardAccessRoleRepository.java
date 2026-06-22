package com.kat.backend.guild.repository;

import com.kat.backend.guild.entity.DashboardAccessRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DashboardAccessRoleRepository
        extends JpaRepository<DashboardAccessRole, DashboardAccessRole.Id> {

    List<DashboardAccessRole> findByIdGuildId(String guildId);

    void deleteByIdGuildId(String guildId);
}
