package com.kat.backend.logging.entity;

import java.io.Serializable;
import java.util.Objects;

public class LoggingEntryId implements Serializable {
    private String config;
    private String logId;

    public LoggingEntryId() {}
    public LoggingEntryId(String config, String logId) {
        this.config = config;
        this.logId = logId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoggingEntryId)) return false;
        LoggingEntryId that = (LoggingEntryId) o;
        return Objects.equals(config, that.config) && Objects.equals(logId, that.logId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(config, logId);
    }
}