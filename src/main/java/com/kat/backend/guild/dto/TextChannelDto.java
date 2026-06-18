package com.kat.backend.guild.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TextChannelDto {
    private String id;
    private String name;
    private boolean nsfw;
    private int slowmodeDelay;
    private String parentCategoryId;
    private String topic;
}