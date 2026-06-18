package com.kat.backend.guild.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guild_auto_roles_config")
@Getter @Setter @NoArgsConstructor
public class GuildAutoRolesConfig {

    @Id
    private String guildId;


    private boolean joinEnabled = false;

    @ElementCollection
    @CollectionTable(
            name = "autoroles_join_roles",
            joinColumns = @JoinColumn(name = "guild_id")
    )
    @Column(name = "role_id")
    private List<String> joinRoleIds = new ArrayList<>();


    private boolean boostEnabled = false;

    @ElementCollection
    @CollectionTable(
            name = "autoroles_boost_roles",
            joinColumns = @JoinColumn(name = "guild_id")
    )
    @Column(name = "role_id")
    private List<String> boostRoleIds = new ArrayList<>();


    private boolean reactionEnabled = false;
    private String reactionChannelId;
    private String reactionMessageContent;
    private String reactionMessageId;
    private boolean reactionUseEmbed = false;
    private String reactionEmbedTitle;
    private String reactionEmbedColor;

    @ElementCollection
    @CollectionTable(
            name = "autoroles_reaction_mappings",
            joinColumns = @JoinColumn(name = "guild_id")
    )
    private List<ReactionRoleMapping> reactionMappings = new ArrayList<>();
}