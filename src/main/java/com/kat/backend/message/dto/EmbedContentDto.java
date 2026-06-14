package com.kat.backend.message.dto;

import lombok.Data;

@Data
public class EmbedContentDto {
    private String channelId;
    private String title;
    private String description;
    private String color;
    private String imageUrl;
    private String thumbnailUrl;
    private String footer;
    private boolean timestamp;
}
