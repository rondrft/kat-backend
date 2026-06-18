package com.kat.backend.guild.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RoleDto {
    private String id;
    private String name;
    private String color;
    private long permissions;
    private int position;
    private boolean hoisted;
    private boolean mentionable;
    private boolean managed;
}