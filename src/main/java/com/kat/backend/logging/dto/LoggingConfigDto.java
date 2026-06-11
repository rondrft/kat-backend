package com.kat.backend.logging.dto;

import lombok.Data;
import java.util.List;

@Data
public class LoggingConfigDto {
    private String defaultChannel;
    private List<LoggingEntryDto> entries;
}