CREATE TABLE IF NOT EXISTS users (
                                     id                   UUID          NOT NULL,
                                     avatar               VARCHAR(255),
    created_at           TIMESTAMP(6)  NOT NULL,
    discord_id           VARCHAR(255)  NOT NULL,
    email                VARCHAR(255),
    email_verified       BOOLEAN,
    last_login           TIMESTAMP(6),
    username             VARCHAR(255),
    discord_access_token VARCHAR(255),
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT uk8h3kehimxhos38stbts56bkba UNIQUE (discord_id)
    );