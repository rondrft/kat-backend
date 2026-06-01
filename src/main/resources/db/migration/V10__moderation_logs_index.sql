DROP INDEX IF EXISTS idx_moderation_logs_created_at;

CREATE INDEX idx_moderation_logs_guild_created
    ON moderation_logs (guild_id, created_at DESC);

CREATE INDEX idx_moderation_logs_user
    ON moderation_logs (guild_id, user_id, created_at DESC);