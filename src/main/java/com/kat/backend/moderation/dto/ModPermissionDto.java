package com.kat.backend.moderation.dto;

import lombok.Data;
import java.util.List;

@Data
public class ModPermissionDto {
    private List<String> xkick;
    private List<String> xban;
    private List<String> xmute;
    private List<String> xunmute;
    private List<String> xwarn;
    private List<String> xhistory;
}