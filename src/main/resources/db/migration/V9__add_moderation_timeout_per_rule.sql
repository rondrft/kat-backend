ALTER TABLE moderation_rule_config
    ADD COLUMN timeout_minutes INTEGER NULL;

ALTER TYPE moderation_action ADD VALUE IF NOT EXISTS 'DELETE_AND_TIMEOUT';