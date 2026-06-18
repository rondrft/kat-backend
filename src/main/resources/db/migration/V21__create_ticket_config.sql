CREATE TABLE IF NOT EXISTS ticket_config (
    guild_id                     VARCHAR(255) NOT NULL,
    enabled                      BOOLEAN      NOT NULL DEFAULT false,
    panel_channel_id             VARCHAR(255),
    panel_message_id             VARCHAR(255),
    category_id                  VARCHAR(255),
    button_label                 VARCHAR(255) NOT NULL DEFAULT 'Create Ticket',
    button_style                 VARCHAR(50)  NOT NULL DEFAULT 'PRIMARY',
    embed_title                  VARCHAR(255) NOT NULL DEFAULT 'Support Tickets',
    embed_description            TEXT         NOT NULL DEFAULT 'Click the button below to create a support ticket.',
    embed_color                  VARCHAR(50)  NOT NULL DEFAULT '#5865F2',
    transcript_enabled           BOOLEAN      NOT NULL DEFAULT false,
    allow_user_to_close          BOOLEAN      NOT NULL DEFAULT true,
    max_open_tickets_per_user    INTEGER      NOT NULL DEFAULT 1,
    ticket_channel_name_template VARCHAR(255) NOT NULL DEFAULT 'ticket-{username}',
    welcome_message              TEXT,
    PRIMARY KEY (guild_id)
);

CREATE TABLE IF NOT EXISTS ticket_support_roles (
    guild_id VARCHAR(255) NOT NULL,
    role_id  VARCHAR(255) NOT NULL,
    PRIMARY KEY (guild_id, role_id),
    FOREIGN KEY (guild_id) REFERENCES ticket_config (guild_id) ON DELETE CASCADE
);
