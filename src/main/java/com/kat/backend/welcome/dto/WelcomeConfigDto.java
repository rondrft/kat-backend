package com.kat.backend.welcome.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WelcomeConfigDto {

    private String guildId;

    private boolean welcomeEnabled;
    private boolean boostEnabled;

    private String welcomeChannelId;
    private String boostChannelId;

    private String welcomeOutputType;
    private String boostOutputType;

    @Size(max = 500)
    private String messageTemplate;

    @Size(max = 256)
    private String embedTitleTemplate;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "embedColor must be a valid hex color")
    private String embedColor;

    @Size(max = 256)
    private String imageTitleTemplate;

    @Size(max = 256)
    private String imageUsernameTemplate;

    @Size(max = 256)
    private String imageFooterTemplate;

    private String imageBackgroundMode;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "imageBackgroundColor must be a valid hex color")
    private String imageBackgroundColor;

    @Size(max = 512)
    private String imageBackgroundUrl;

    private boolean imageShowAvatar;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "imageTextColor must be a valid hex color")
    private String imageTextColor;

    private boolean imageMentionUser;

    private boolean imageCardEnabled;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "imageCardColor must be a valid hex color")
    private String imageCardColor;

    private int imageCardOpacity;
    private int imageAvatarSize;
}