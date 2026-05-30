CREATE TABLE IF NOT EXISTS guild_temp_voice_config (
                                                       guild_id              VARCHAR(255) NOT NULL,
    category_id           VARCHAR(255),
    channel_name_template VARCHAR(255),
    delete_delay_seconds  INTEGER,
    enabled               BOOLEAN      NOT NULL,
    hub_channel_id        VARCHAR(255),
    locked_to_owner       BOOLEAN,
    user_limit            INTEGER,
    CONSTRAINT guild_temp_voice_config_pkey PRIMARY KEY (guild_id)
    );