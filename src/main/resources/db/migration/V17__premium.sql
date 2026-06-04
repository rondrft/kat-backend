CREATE TABLE IF NOT EXISTS premium_guilds (
                                              guild_id   VARCHAR(20)  NOT NULL,
    expires_at TIMESTAMP,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT premium_guilds_pkey PRIMARY KEY (guild_id)
    );