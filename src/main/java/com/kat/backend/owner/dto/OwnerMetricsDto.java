package com.kat.backend.owner.dto;

import lombok.Data;

@Data
public class OwnerMetricsDto {
    private int totalGuilds;
    private int totalUsers;
    private int totalChannels;
    private long gatewayPingMs;
    private long uptimeSeconds;
    private long jvmUsedMemoryMb;
    private long jvmMaxMemoryMb;
    private int activeThreads;
    private long totalAuditLogs;
    private long totalWarns;
    private long totalMessageCounts;
    private long totalGuildMembers;
    private long todayMessagesProcessed;
    private long todayCommandsExecuted;
    private long todayAutomodActions;
    private long todayMembersJoined;
    private long todayAuditActions;
    private long todayWelcomeSent;
    private long todayWarnsIssued;
    private long todayMutesIssued;
    private long todayBansIssued;
    private long todayKicksIssued;
}