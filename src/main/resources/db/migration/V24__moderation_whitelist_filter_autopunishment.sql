ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'REPETITION';
ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'WALL_OF_TEXT';
ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'NEWLINES';
ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'SPOILERS';
ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'EVERYONE_HERE';
ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'FORMATTING';
ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'EMOJIS';
ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'BAD_WORDS';
ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'PHISHING';
ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'MASS_MENTION';
ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'IMAGE_SPAM';
ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'COPY_PASTA';
ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'ACCOUNT_AGE';
ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'JOIN_RAID';
ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'CHANNEL_RAID';
ALTER TYPE moderation_rule_type ADD VALUE IF NOT EXISTS 'ROLE_RAID';

ALTER TYPE moderation_action ADD VALUE IF NOT EXISTS 'WARN';
ALTER TYPE moderation_action ADD VALUE IF NOT EXISTS 'KICK';
ALTER TYPE moderation_action ADD VALUE IF NOT EXISTS 'BAN';

ALTER TABLE moderation_config ADD COLUMN IF NOT EXISTS log_channel_id VARCHAR(20);
ALTER TABLE moderation_config ADD COLUMN IF NOT EXISTS premium_log_channel_id VARCHAR(20);

CREATE TABLE IF NOT EXISTS moderation_whitelist (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id     VARCHAR(20) NOT NULL,
    channel_id   VARCHAR(20),
    user_id      VARCHAR(20),
    category_id  VARCHAR(20),
    rule_type    moderation_rule_type,
    reason       TEXT,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_moderation_whitelist_guild ON moderation_whitelist(guild_id);

CREATE TABLE IF NOT EXISTS moderation_filter (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id    VARCHAR(20) NOT NULL,
    pattern     VARCHAR(500) NOT NULL,
    enabled     BOOLEAN NOT NULL DEFAULT TRUE,
    action      moderation_action,
    replacement TEXT,
    reason      TEXT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_moderation_filter_guild ON moderation_filter(guild_id);

CREATE TABLE IF NOT EXISTS moderation_auto_punishment (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guild_id        VARCHAR(20) NOT NULL,
    rule_type       moderation_rule_type NOT NULL,
    threshold       INTEGER NOT NULL DEFAULT 5,
    action          moderation_action NOT NULL,
    timeout_minutes INTEGER,
    enabled         BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (guild_id, rule_type)
);
CREATE INDEX IF NOT EXISTS idx_moderation_auto_punishment_guild ON moderation_auto_punishment(guild_id);
