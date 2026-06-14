package com.kat.backend.message.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class MessageContentDto {

    @NotBlank(message = "Channel ID is required")
    private String channelId;

    @NotEmpty(message = "At least one line is required")
    @Size(max = 20, message = "Message cannot exceed 20 lines")
    @Valid
    private List<MessageFormatDto> formats;
}
