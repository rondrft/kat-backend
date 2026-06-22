CREATE TABLE IF NOT EXISTS dashboard_access_users (
    guild_id VARCHAR(255) NOT NULL,
    user_id  VARCHAR(255) NOT NULL,
    PRIMARY KEY (guild_id, user_id)
);

CREATE TABLE IF NOT EXISTS dashboard_access_roles (
    guild_id VARCHAR(255) NOT NULL,
    role_id  VARCHAR(255) NOT NULL,
    PRIMARY KEY (guild_id, role_id)
);
