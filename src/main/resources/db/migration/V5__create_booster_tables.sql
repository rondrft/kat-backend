CREATE TABLE IF NOT EXISTS booster_custom_roles (
                                                    id               UUID         NOT NULL,
                                                    created_at       TIMESTAMP(6),
    discord_role_id  VARCHAR(255),
    guild_id         VARCHAR(255) NOT NULL,
    owner_discord_id VARCHAR(255) NOT NULL,
    role_color       VARCHAR(255),
    role_emoji       VARCHAR(255),
    role_name        VARCHAR(255),
    CONSTRAINT booster_custom_roles_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS booster_records (
                                               id               UUID         NOT NULL,
                                               boost_count      INTEGER      NOT NULL,
                                               discord_user_id  VARCHAR(255) NOT NULL,
    guild_id         VARCHAR(255) NOT NULL,
    CONSTRAINT booster_records_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS booster_config (
                                              guild_id        VARCHAR(255) NOT NULL,
    enabled         BOOLEAN      NOT NULL DEFAULT false,
    required_boosts INTEGER      NOT NULL DEFAULT 2,
    allow_invites   BOOLEAN      NOT NULL DEFAULT true,
    CONSTRAINT booster_config_pkey PRIMARY KEY (guild_id)
    );