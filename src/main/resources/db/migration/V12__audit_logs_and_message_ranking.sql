CREATE TYPE audit_action AS ENUM (
    'BAN', 'UNBAN', 'KICK', 'TIMEOUT', 'UNTIMEOUT',
    'MUTE', 'UNMUTE', 'WARN', 'MESSAGE_DELETE', 'MESSAGE_BULK_DELETE',
    'ROLE_ADD', 'ROLE_REMOVE', 'AUTO_MOD'
);

CREATE TABLE audit_logs (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            guild_id VARCHAR(20) NOT NULL,
                            target_discord_id VARCHAR(20),
                            target_username VARCHAR(100),
                            target_avatar VARCHAR(256),
                            executor_discord_id VARCHAR(20),
                            executor_username VARCHAR(100),
                            action audit_action NOT NULL,
                            reason TEXT,
                            duration_minutes INTEGER,
                            created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_audit_logs_guild_created ON audit_logs (guild_id, created_at DESC);
CREATE INDEX idx_audit_logs_guild_target ON audit_logs (guild_id, target_discord_id, created_at DESC);

CREATE TABLE message_counts (
                                guild_id VARCHAR(20) NOT NULL,
                                discord_user_id VARCHAR(20) NOT NULL,
                                username VARCHAR(100),
                                avatar VARCHAR(256),
                                message_count BIGINT NOT NULL DEFAULT 0,
                                last_message_at TIMESTAMPTZ,
                                PRIMARY KEY (guild_id, discord_user_id)
);

CREATE INDEX idx_message_counts_guild_count ON message_counts (guild_id, message_count DESC);