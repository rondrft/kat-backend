package com.kat.backend.guild.service;

import com.kat.backend.guild.entity.GuildConfig;
import com.kat.backend.guild.repository.GuildConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GuildConfigService {

    private final GuildConfigRepository repository;

    @Transactional
    public GuildConfig getConfig(String guildId) {
        return repository.findById(guildId)
                .orElseGet(() -> {
                    try {
                        GuildConfig config = GuildConfig.builder()
                                .guildId(guildId)
                                .prefix("x")
                                .locale("en")
                                .build();
                        return repository.save(config);
                    } catch (DataIntegrityViolationException e) {
                        return repository.findById(guildId).orElseThrow();
                    }
                });
    }

    public GuildConfig updateConfig(String guildId, String prefix, String locale) {
        GuildConfig config = repository.findById(guildId)
                .orElse(GuildConfig.builder()
                        .guildId(guildId)
                        .prefix("x")
                        .locale("en")
                        .build());

        if (prefix != null) config.setPrefix(prefix);
        if (locale != null) config.setLocale(locale);

        return repository.save(config);
    }
}
