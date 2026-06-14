package com.kat.backend.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MessageFormatDto {

    @NotBlank(message = "Format type is required")
    @Pattern(regexp = "^(normal|large|underlined|quote|spoiler)$", message = "Invalid format type")
    private String type;

    @NotBlank(message = "Content is required")
    @Size(max = 2000, message = "Line cannot exceed 2000 characters")
    private String content;
}
