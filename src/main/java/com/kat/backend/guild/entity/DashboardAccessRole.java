package com.kat.backend.guild.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dashboard_access_roles")
public class DashboardAccessRole {

    @EmbeddedId
    private Id id;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Id implements Serializable {
        @Column(name = "guild_id")
        private String guildId;

        @Column(name = "role_id")
        private String roleId;
    }
}
