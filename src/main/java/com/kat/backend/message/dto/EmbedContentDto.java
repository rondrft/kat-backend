package com.kat.backend.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmbedContentDto {

    @NotBlank(message = "Channel ID is required")
    private String channelId;

    @Size(max = 256, message = "Title cannot exceed 256 characters")
    private String title;

    @Size(max = 4096, message = "Description cannot exceed 4096 characters")
    private String description;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a valid hex color")
    private String color;

    @Pattern(regexp = "^(https?://.*)?$", message = "Image URL must be http or https")
    private String imageUrl;

    @Pattern(regexp = "^(https?://.*)?$", message = "Thumbnail URL must be http or https")
    private String thumbnailUrl;

    @Size(max = 256, message = "Footer cannot exceed 256 characters")
    private String footer;

    private boolean timestamp;
}
