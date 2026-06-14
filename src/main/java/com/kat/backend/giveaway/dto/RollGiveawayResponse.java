package com.kat.backend.giveaway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RollGiveawayResponse {

    private String giveawayId;
    private List<GiveawayParticipantDto> winners;
    private int totalParticipants;
    private boolean ended;
}
