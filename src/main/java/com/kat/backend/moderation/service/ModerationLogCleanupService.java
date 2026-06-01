package com.kat.backend.moderation.service;

import com.kat.backend.moderation.repository.ModerationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModerationLogCleanupService {

    private static final int RETENTION_DAYS = 30;

    private final ModerationLogRepository repository;

    @Scheduled(cron = "0 0 3 * * *") // every day at 3am UTC
    @Transactional
    public void cleanOldLogs() {
        Instant cutoff = Instant.now().minus(RETENTION_DAYS, ChronoUnit.DAYS);
        int deleted = repository.deleteOlderThan(cutoff);
        log.info("Moderation log cleanup: deleted {} entries older than {} days", deleted, RETENTION_DAYS);
    }
}