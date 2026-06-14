package com.kat.backend.message.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SendMessageRequest {

    @NotBlank(message = "Type is required")
    @Pattern(regexp = "^(message|embed)$", message = "Type must be message or embed")
    private String type;

    @NotBlank(message = "Guild ID is required")
    private String guildId;

    @NotBlank(message = "Channel ID is required")
    private String channelId;

    @Valid
    private MessageContentDto messageContent;

    @Valid
    private EmbedContentDto embedContent;
}
