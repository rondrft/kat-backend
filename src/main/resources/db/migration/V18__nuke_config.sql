CREATE TABLE IF NOT EXISTS nuke_config (
                                           guild_id        VARCHAR(20) NOT NULL,
    allowed_role_id VARCHAR(20),
    allowed_user_ids TEXT[] NOT NULL DEFAULT '{}',
    cooldown_seconds INTEGER NOT NULL DEFAULT 300,
    CONSTRAINT nuke_config_pkey PRIMARY KEY (guild_id)
    );