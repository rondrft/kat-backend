package com.kat.backend.guild.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "guild_temp_voice_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuildTempVoiceConfig {

    @Id
    @Column(name = "guild_id")
    private String guildId;

    private boolean enabled;

    @Column(name = "hub_channel_id")
    private String hubChannelId;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "channel_name_template")
    private String channelNameTemplate;

    @Column(name = "user_limit")
    private int userLimit;

    @Column(name = "delete_delay_seconds")
    private int deleteDelaySeconds;

    @Column(name = "locked_to_owner")
    private boolean lockedToOwner;
}