CREATE INDEX IF NOT EXISTS idx_booster_custom_roles_guild_owner
    ON booster_custom_roles (guild_id, owner_discord_id);

CREATE INDEX IF NOT EXISTS idx_booster_custom_roles_guild
    ON booster_custom_roles (guild_id);

CREATE INDEX IF NOT EXISTS idx_guild_members_user
    ON guild_members (discord_user_id);

CREATE INDEX IF NOT EXISTS idx_guild_members_guild_joined
    ON guild_members (guild_id, joined_at DESC);
