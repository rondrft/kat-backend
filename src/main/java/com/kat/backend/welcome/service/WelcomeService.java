package com.kat.backend.welcome.service;

import com.kat.backend.welcome.dto.WelcomeConfigDto;
import com.kat.backend.welcome.entity.WelcomeConfig;
import com.kat.backend.welcome.mapper.WelcomeConfigMapper;
import com.kat.backend.welcome.repository.WelcomeConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WelcomeService {

    private static final List<String> ALLOWED_TYPES = List.of(
            "image/png", "image/jpeg", "image/webp"
    );

    private static final List<byte[]> MAGIC_BYTES = List.of(
            new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47},
            new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF},
            new byte[]{0x52, 0x49, 0x46, 0x46}
    );

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final String UPLOAD_SUBPATH = "uploads/backgrounds/";

    private boolean isValidImageSignature(byte[] fileBytes) {
        for (byte[] magic : MAGIC_BYTES) {
            if (fileBytes.length >= magic.length) {
                byte[] header = Arrays.copyOf(fileBytes, magic.length);
                if (Arrays.equals(header, magic)) return true;
            }
        }
        return false;
    }

    @Value("${app.base-url}")
    private String appBaseUrl;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    private final WelcomeConfigRepository repository;
    private final WelcomeConfigMapper mapper;

    @Cacheable(value = "welcomeConfigs", key = "#guildId", unless = "#result == null")
    public WelcomeConfigDto getConfig(String guildId) {
        return repository.findById(guildId)
                .map(mapper::toDto)
                .orElseGet(() -> mapper.toDto(mapper.defaults(guildId)));
    }

    @CacheEvict(value = "welcomeConfigs", key = "#guildId")
    public WelcomeConfigDto saveConfig(String guildId, WelcomeConfigDto dto) {
        WelcomeConfig existing = repository.findById(guildId)
                .orElse(mapper.defaults(guildId));

        WelcomeConfig updated = mapper.toEntity(guildId, dto);

        if (updated.getImageBackgroundUrl() == null && existing.getImageBackgroundUrl() != null) {
            updated.setImageBackgroundUrl(existing.getImageBackgroundUrl());
        }

        return mapper.toDto(repository.save(updated));
    }

    @CacheEvict(value = "welcomeConfigs", key = "#guildId")
    public String saveBackground(String guildId, MultipartFile file) throws IOException {
        if (file.isEmpty()) throw new IllegalArgumentException("File is empty");

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType))
            throw new IllegalArgumentException("Invalid file type. Allowed: png, jpeg, webp");

        if (file.getSize() > MAX_FILE_SIZE)
            throw new IllegalArgumentException("File exceeds maximum size of 5MB");

        byte[] fileBytes = file.getBytes();
        if (!isValidImageSignature(fileBytes))
            throw new IllegalArgumentException("File content does not match a valid image format");

        Path uploadPath = Paths.get(uploadDir, "backgrounds");
        Files.createDirectories(uploadPath);

        String extension = contentType.split("/")[1];
        String filename = guildId + "_" + UUID.randomUUID() + "." + extension;
        Path filePath = uploadPath.resolve(filename);
        Files.write(filePath, fileBytes);

        String url = appBaseUrl + "/uploads/backgrounds/" + filename;

        WelcomeConfig config = repository.findById(guildId)
                .orElse(mapper.defaults(guildId));
        config.setImageBackgroundUrl(url);
        repository.save(config);

        return url;
    }
}