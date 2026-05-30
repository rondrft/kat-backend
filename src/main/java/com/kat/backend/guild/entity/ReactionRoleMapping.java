package com.kat.backend.guild.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter @Setter @NoArgsConstructor
public class ReactionRoleMapping {

    private String emoji;
    private String roleId;
}