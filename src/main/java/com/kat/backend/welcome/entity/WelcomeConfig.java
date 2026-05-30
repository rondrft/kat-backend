package com.kat.backend.welcome.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "welcome_config")
public class WelcomeConfig {

    @Id
    @Column(name = "guild_id", nullable = false)
    private String guildId;

    @Column(name = "welcome_enabled", nullable = false)
    private boolean welcomeEnabled;

    @Column(name = "boost_enabled", nullable = false)
    private boolean boostEnabled;

    @Column(name = "welcome_channel_id")
    private String welcomeChannelId;

    @Column(name = "boost_channel_id")
    private String boostChannelId;

    @Column(name = "welcome_output_type", nullable = false)
    private String welcomeOutputType;

    @Column(name = "boost_output_type", nullable = false)
    private String boostOutputType;

    @Column(name = "message_template", nullable = false)
    private String messageTemplate;

    @Column(name = "embed_title_template")
    private String embedTitleTemplate;

    @Column(name = "embed_color")
    private String embedColor;

    @Column(name = "image_title_template", nullable = false)
    private String imageTitleTemplate;

    @Column(name = "image_username_template", nullable = false)
    private String imageUsernameTemplate;

    @Column(name = "image_footer_template", nullable = false)
    private String imageFooterTemplate;

    @Column(name = "image_background_mode", nullable = false)
    private String imageBackgroundMode;

    @Column(name = "image_background_color", nullable = false)
    private String imageBackgroundColor;

    @Column(name = "image_background_url")
    private String imageBackgroundUrl;

    @Column(name = "image_show_avatar", nullable = false)
    private boolean imageShowAvatar;

    @Column(name = "image_text_color", nullable = false)
    private String imageTextColor;

    @Column(name = "image_mention_user", nullable = false)
    private boolean imageMentionUser;
}