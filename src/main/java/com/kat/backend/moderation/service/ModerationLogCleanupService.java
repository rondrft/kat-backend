package com.kat.backend.moderation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModerationLogCleanupService {

    private static final int RETENTION_DAYS = 30;

    private final ModerationLogCleanupTransactionService transactionService;

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanOldLogs() {
        Instant cutoff = Instant.now().minus(RETENTION_DAYS, ChronoUnit.DAYS);
        int totalDeleted = 0;
        int deleted;
        do {
            deleted = transactionService.deleteNextBatch(cutoff);
            totalDeleted += deleted;
        } while (deleted == ModerationLogCleanupTransactionService.BATCH_SIZE);
        log.info("Moderation log cleanup: deleted {} entries older than {} days", totalDeleted, RETENTION_DAYS);
    }
}