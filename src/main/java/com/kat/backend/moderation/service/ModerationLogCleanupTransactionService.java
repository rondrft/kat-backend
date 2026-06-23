package com.kat.backend.moderation.service;

import com.kat.backend.moderation.repository.ModerationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModerationLogCleanupTransactionService {

    static final int BATCH_SIZE = 1000;

    private final ModerationLogRepository repository;

    @Transactional
    public int deleteNextBatch(Instant cutoff) {
        return repository.deleteOlderThan(cutoff, BATCH_SIZE);
    }
}