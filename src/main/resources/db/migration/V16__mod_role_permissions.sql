CREATE TABLE mod_role_permissions (
                                      guild_id    VARCHAR(20) NOT NULL,
                                      role_id     VARCHAR(20) NOT NULL,
                                      command     VARCHAR(20) NOT NULL,
                                      PRIMARY KEY (guild_id, role_id, command)
);

CREATE INDEX idx_mod_role_permissions_guild ON mod_role_permissions (guild_id);

CREATE TABLE purge_config (
                              guild_id        VARCHAR(20) PRIMARY KEY,
                              enabled         BOOLEAN NOT NULL DEFAULT false,
                              allowed_role_id VARCHAR(20),
                              max_messages    INTEGER NOT NULL DEFAULT 20,
                              max_age_seconds INTEGER NOT NULL DEFAULT 300
);