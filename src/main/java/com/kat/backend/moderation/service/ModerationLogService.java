package com.kat.backend.moderation.service;

import com.kat.backend.moderation.dto.ModerationLogDto;
import com.kat.backend.moderation.dto.ModerationLogPageDto;
import com.kat.backend.moderation.entity.ModerationLog;
import com.kat.backend.moderation.repository.ModerationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModerationLogService {

    private static final int MAX_PAGE_SIZE = 50;

    private final ModerationLogRepository repository;

    @Transactional(readOnly = true)
    public ModerationLogPageDto getLogs(String guildId, int page, int size) {
        int safeSize = Math.min(size, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, safeSize);
        Page<ModerationLog> result = repository.findByGuildId(guildId, pageable);

        return ModerationLogPageDto.builder()
                .content(result.getContent().stream().map(this::toDto).toList())
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .last(result.isLast())
                .build();
    }

    private ModerationLogDto toDto(ModerationLog log) {
        return ModerationLogDto.builder()
                .id(log.getId())
                .guildId(log.getGuildId())
                .userId(log.getUserId())
                .channelId(log.getChannelId())
                .ruleType(log.getRuleType())
                .action(log.getAction())
                .reason(log.getReason())
                .success(log.isSuccess())
                .createdAt(log.getCreatedAt())
                .build();
    }
}