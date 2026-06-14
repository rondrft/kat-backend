package com.kat.backend.giveaway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiveawayResponse {

    private String id;
    private String guildId;
    private String channelId;
    private String messageId;
    private String prize;
    private Instant endTime;
    private int winnerCount;
    private boolean boosterOnly;
    private Set<String> participantIds;
    private List<String> winnerIds;
    private boolean ended;
    private boolean active;
    private boolean startedFromDashboard;
}
