CREATE TYPE moderation_rule_type AS ENUM ('SPAM', 'LINKS', 'INVITES', 'MENTIONS', 'CAPS');
CREATE TYPE moderation_action AS ENUM ('MONITOR', 'DELETE', 'TIMEOUT');

CREATE TABLE moderation_config (
                                   guild_id VARCHAR(255) PRIMARY KEY,
                                   enabled BOOLEAN NOT NULL DEFAULT FALSE,
                                   strictness INTEGER NOT NULL DEFAULT 50,
                                   default_timeout_minutes INTEGER NOT NULL DEFAULT 10,
                                   created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                   updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE moderation_rule_config (
                                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                        guild_id VARCHAR(255) NOT NULL REFERENCES moderation_config(guild_id) ON DELETE CASCADE,
                                        rule_type moderation_rule_type NOT NULL,
                                        enabled BOOLEAN NOT NULL DEFAULT FALSE,
                                        action moderation_action NOT NULL DEFAULT 'MONITOR',
                                        threshold INTEGER NOT NULL DEFAULT 5,
                                        UNIQUE (guild_id, rule_type)
);

CREATE TABLE moderation_logs (
                                 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 guild_id VARCHAR(255) NOT NULL,
                                 user_id VARCHAR(255) NOT NULL,
                                 message_id VARCHAR(255),
                                 channel_id VARCHAR(255),
                                 rule_type moderation_rule_type NOT NULL,
                                 action moderation_action NOT NULL,
                                 reason VARCHAR(500),
                                 content_preview VARCHAR(200),
                                 success BOOLEAN NOT NULL DEFAULT TRUE,
                                 created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_moderation_logs_guild_id ON moderation_logs(guild_id);
CREATE INDEX idx_moderation_logs_user_id ON moderation_logs(user_id);
CREATE INDEX idx_moderation_logs_created_at ON moderation_logs(created_at);