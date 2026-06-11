CREATE TABLE IF NOT EXISTS logging_config (
                                              guild_id        VARCHAR(20) NOT NULL,
    default_channel VARCHAR(20),
    CONSTRAINT logging_config_pkey PRIMARY KEY (guild_id)
    );

CREATE TABLE IF NOT EXISTS logging_entries (
                                               guild_id   VARCHAR(20)  NOT NULL,
    log_id     VARCHAR(50)  NOT NULL,
    enabled    BOOLEAN      NOT NULL DEFAULT false,
    channel_id VARCHAR(20),
    CONSTRAINT logging_entries_pkey PRIMARY KEY (guild_id, log_id),
    CONSTRAINT fk_logging_entries_guild FOREIGN KEY (guild_id)
    REFERENCES logging_config (guild_id) ON DELETE CASCADE
    );