package com.kat.backend.welcome.service;

import com.kat.backend.welcome.dto.WelcomeConfigDto;
import com.kat.backend.welcome.entity.WelcomeConfig;
import com.kat.backend.welcome.mapper.WelcomeConfigMapper;
import com.kat.backend.welcome.repository.WelcomeConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WelcomeService {

    private static final List<String> ALLOWED_TYPES = List.of(
            "image/png", "image/jpeg", "image/webp"
    );
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final String UPLOAD_SUBPATH = "uploads/backgrounds/";

    @Value("${app.base-url}")
    private String appBaseUrl;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    private final WelcomeConfigRepository repository;
    private final WelcomeConfigMapper mapper;

    public WelcomeConfigDto getConfig(String guildId) {
        return repository.findById(guildId)
                .map(mapper::toDto)
                .orElseGet(() -> mapper.toDto(mapper.defaults(guildId)));
    }

    public WelcomeConfigDto saveConfig(String guildId, WelcomeConfigDto dto) {
        WelcomeConfig existing = repository.findById(guildId)
                .orElse(mapper.defaults(guildId));

        WelcomeConfig updated = mapper.toEntity(guildId, dto);

        if (updated.getImageBackgroundUrl() == null && existing.getImageBackgroundUrl() != null) {
            updated.setImageBackgroundUrl(existing.getImageBackgroundUrl());
        }

        return mapper.toDto(repository.save(updated));
    }

    public String saveBackground(String guildId, MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("File is empty");

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType))
            throw new IllegalArgumentException("Invalid file type. Allowed: png, jpeg, webp");

        if (file.getSize() > MAX_FILE_SIZE)
            throw new IllegalArgumentException("File exceeds maximum size of 5MB");

        Path uploadPath = Paths.get(uploadDir, "backgrounds");
        Files.createDirectories(uploadPath);

        String extension = contentType.split("/")[1];
        String filename = guildId + "_" + UUID.randomUUID() + "." + extension;
        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, file.getBytes());

        String url = appBaseUrl + "/uploads/backgrounds/" + filename;

        WelcomeConfig config = repository.findById(guildId)
                .orElse(mapper.defaults(guildId));
        config.setImageBackgroundUrl(url);
        repository.save(config);

        return url;
    }
}