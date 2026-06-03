ALTER TABLE welcome_config
    ADD COLUMN image_card_enabled boolean NOT NULL DEFAULT true,
    ADD COLUMN image_card_color varchar(9) NOT NULL DEFAULT '#000000',
    ADD COLUMN image_card_opacity integer NOT NULL DEFAULT 120,
    ADD COLUMN image_avatar_size integer NOT NULL DEFAULT 140;