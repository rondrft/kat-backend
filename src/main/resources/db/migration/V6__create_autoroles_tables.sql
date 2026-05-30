CREATE TABLE IF NOT EXISTS guild_auto_roles_config (
                                                       guild_id                 VARCHAR(255) NOT NULL,
    boost_enabled            BOOLEAN      NOT NULL,
    join_enabled             BOOLEAN      NOT NULL,
    reaction_enabled         BOOLEAN      NOT NULL,
    reaction_message_id      VARCHAR(255),
    reaction_channel_id      VARCHAR(255),
    reaction_message_content VARCHAR(255),
    reaction_embed_color     VARCHAR(255),
    reaction_embed_title     VARCHAR(255),
    reaction_use_embed       BOOLEAN      NOT NULL DEFAULT false,
    CONSTRAINT guild_auto_roles_config_pkey PRIMARY KEY (guild_id)
    );

CREATE TABLE IF NOT EXISTS autoroles_join_roles (
                                                    guild_id VARCHAR(255) NOT NULL,
    role_id  VARCHAR(255),
    CONSTRAINT fk30fnbkeflxy68sypq35cq4vdp
    FOREIGN KEY (guild_id) REFERENCES guild_auto_roles_config(guild_id)
    );

CREATE TABLE IF NOT EXISTS autoroles_boost_roles (
                                                     guild_id VARCHAR(255) NOT NULL,
    role_id  VARCHAR(255),
    CONSTRAINT fkpx9961l6wmne2spv62cigxhex
    FOREIGN KEY (guild_id) REFERENCES guild_auto_roles_config(guild_id)
    );

CREATE TABLE IF NOT EXISTS autoroles_reaction_mappings (
                                                           guild_id VARCHAR(255) NOT NULL,
    emoji    VARCHAR(255),
    role_id  VARCHAR(255),
    CONSTRAINT fkge0gn65jbxuq4rgx3kd3eob5d
    FOREIGN KEY (guild_id) REFERENCES guild_auto_roles_config(guild_id)
    );