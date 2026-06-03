CREATE TABLE warns (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       guild_id VARCHAR(20) NOT NULL,
                       target_discord_id VARCHAR(20) NOT NULL,
                       executor_discord_id VARCHAR(20) NOT NULL,
                       executor_username VARCHAR(100),
                       reason TEXT NOT NULL,
                       created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_warns_guild_target ON warns (guild_id, target_discord_id, created_at DESC);