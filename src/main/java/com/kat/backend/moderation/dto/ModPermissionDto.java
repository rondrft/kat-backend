package com.kat.backend.moderation.dto;

import lombok.Data;
import java.util.List;

@Data
public class ModPermissionDto {
    private List<String> xkick;
    private List<String> xban;
    private List<String> xsoftban;
    private List<String> xtempban;
    private List<String> xunban;
    private List<String> xmute;
    private List<String> xunmute;
    private List<String> xwarn;
    private List<String> xhistory;
    private List<String> xwarnings;
    private List<String> xclearwarns;
    private List<String> xnuke;
    private List<String> xslowmode;
    private List<String> xlock;
    private List<String> xunlock;
    private List<String> xfilter;
    private List<String> xwhitelist;
    private List<String> xmodconfig;
}