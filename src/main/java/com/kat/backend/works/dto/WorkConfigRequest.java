package com.kat.backend.works.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkConfigRequest {
    private boolean enabled;
    private List<String> allowedChannelIds;
}
