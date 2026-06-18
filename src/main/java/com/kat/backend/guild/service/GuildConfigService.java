package com.kat.backend.guild.service;

import com.kat.backend.guild.entity.GuildConfig;
import com.kat.backend.guild.repository.GuildConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuildConfigService {

    private final GuildConfigRepository repository;

    public GuildConfig getConfig(String guildId) {
        return repository.findById(guildId)
                .orElse(GuildConfig.builder()
                        .guildId(guildId)
                        .prefix("x")
                        .locale("en")
                        .build());
    }

    public GuildConfig updateConfig(String guildId, String prefix, String locale) {
        GuildConfig config = repository.findById(guildId)
                .orElse(GuildConfig.builder().guildId(guildId).build());

        if (prefix != null) config.setPrefix(prefix);
        if (locale != null) config.setLocale(locale);

        return repository.save(config);
    }
}
