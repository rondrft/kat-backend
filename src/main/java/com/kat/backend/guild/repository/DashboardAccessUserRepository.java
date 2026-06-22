package com.kat.backend.guild.repository;

import com.kat.backend.guild.entity.DashboardAccessUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DashboardAccessUserRepository
        extends JpaRepository<DashboardAccessUser, DashboardAccessUser.Id> {

    List<DashboardAccessUser> findByIdGuildId(String guildId);

    boolean existsByIdGuildIdAndIdUserId(String guildId, String userId);

    void deleteByIdGuildId(String guildId);
}
