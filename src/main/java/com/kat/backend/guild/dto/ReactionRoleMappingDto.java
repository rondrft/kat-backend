package com.kat.backend.guild.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReactionRoleMappingDto {
    private String emoji;
    private String roleId;
}