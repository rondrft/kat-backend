ALTER TABLE payment_orders
    ADD CONSTRAINT uq_mp_payment_id UNIQUE (mercado_pago_payment_id);
