package com.kat.backend.message.dto;

import lombok.Data;

import java.util.List;

@Data
public class MessageContentDto {
    private String channelId;
    private List<MessageFormatDto> formats;
}
