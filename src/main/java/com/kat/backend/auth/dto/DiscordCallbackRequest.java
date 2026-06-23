package com.kat.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DiscordCallbackRequest {

    @NotBlank(message = "The code cannot be empty")
    private String code;

    private String state;
}
