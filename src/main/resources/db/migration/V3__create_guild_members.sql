CREATE TABLE IF NOT EXISTS guild_members (
                                             id                 VARCHAR(255) NOT NULL,
    avatar             VARCHAR(255),
    discord_user_id    VARCHAR(255) NOT NULL,
    guild_id           VARCHAR(255) NOT NULL,
    joined_at          TIMESTAMP(6) NOT NULL,
    username           VARCHAR(255) NOT NULL,
    account_created_at TIMESTAMP(6),
    is_bot             BOOLEAN,
    is_verified_bot    BOOLEAN,
    CONSTRAINT guild_members_pkey PRIMARY KEY (id)
    );

CREATE INDEX IF NOT EXISTS idxodid0gnsk419ri1o3wfnppurp
    ON guild_members (guild_id, joined_at DESC);