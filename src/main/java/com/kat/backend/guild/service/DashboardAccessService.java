package com.kat.backend.guild.service;

import com.kat.backend.guild.entity.DashboardAccessRole;
import com.kat.backend.guild.entity.DashboardAccessUser;
import com.kat.backend.guild.repository.DashboardAccessRoleRepository;
import com.kat.backend.guild.repository.DashboardAccessUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardAccessService {

    private final DashboardAccessUserRepository userRepository;
    private final DashboardAccessRoleRepository roleRepository;

    public List<String> getAllowedUserIds(String guildId) {
        return userRepository.findByIdGuildId(guildId)
                .stream()
                .map(u -> u.getId().getUserId())
                .toList();
    }

    @Cacheable(value = "dashboardRoles", key = "#guildId")
    public List<String> getAllowedRoleIds(String guildId) {
        return roleRepository.findByIdGuildId(guildId)
                .stream()
                .map(r -> r.getId().getRoleId())
                .toList();
    }

    public boolean hasUserAccess(String guildId, String userId) {
        return userRepository.existsByIdGuildIdAndIdUserId(guildId, userId);
    }

    @Transactional
    @CacheEvict(value = "dashboardRoles", key = "#guildId")
    public void saveAccess(String guildId, List<String> allowedUserIds, List<String> allowedRoleIds) {
        userRepository.deleteByIdGuildId(guildId);
        roleRepository.deleteByIdGuildId(guildId);

        if (allowedUserIds != null) {
            userRepository.saveAll(allowedUserIds.stream()
                    .distinct()
                    .map(uid -> new DashboardAccessUser(new DashboardAccessUser.Id(guildId, uid)))
                    .toList());
        }

        if (allowedRoleIds != null) {
            roleRepository.saveAll(allowedRoleIds.stream()
                    .distinct()
                    .map(rid -> new DashboardAccessRole(new DashboardAccessRole.Id(guildId, rid)))
                    .toList());
        }
    }
}
