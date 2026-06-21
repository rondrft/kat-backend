ALTER TABLE premium_guilds
    ADD COLUMN IF NOT EXISTS plan                  VARCHAR(20),
    ADD COLUMN IF NOT EXISTS purchased_by_user_id  VARCHAR(20),
    ADD COLUMN IF NOT EXISTS updated_at            TIMESTAMP;

CREATE TABLE IF NOT EXISTS payment_orders (
    id                         VARCHAR(36)    NOT NULL,
    guild_id                   VARCHAR(20)    NOT NULL,
    user_id                    VARCHAR(20)    NOT NULL,
    plan                       VARCHAR(20)    NOT NULL,
    amount                     NUMERIC(10, 2) NOT NULL,
    status                     VARCHAR(20)    NOT NULL DEFAULT 'PENDING',
    mercado_pago_preference_id VARCHAR(100),
    mercado_pago_payment_id    VARCHAR(50),
    checkout_url               TEXT,
    created_at                 TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at                 TIMESTAMP,
    version                    BIGINT         NOT NULL DEFAULT 0,
    CONSTRAINT payment_orders_pkey PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_payment_orders_guild_user_plan ON payment_orders (guild_id, user_id, plan, status);
CREATE INDEX IF NOT EXISTS idx_payment_orders_mp_payment_id   ON payment_orders (mercado_pago_payment_id);
