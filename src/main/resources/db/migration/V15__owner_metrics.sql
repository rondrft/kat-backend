CREATE TABLE daily_metrics (
                               date        DATE NOT NULL,
                               guild_id    VARCHAR(20),
                               metric_key  VARCHAR(50) NOT NULL,
                               value       BIGINT NOT NULL DEFAULT 0,
                               PRIMARY KEY (date, metric_key, guild_id)
);

CREATE INDEX idx_daily_metrics_date ON daily_metrics (date DESC);