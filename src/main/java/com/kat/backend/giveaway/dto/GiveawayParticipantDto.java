package com.kat.backend.giveaway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiveawayParticipantDto {

    private String userId;
    private String username;
    private String globalName;
    private String avatarUrl;
    private String effectiveName;
    private boolean booster;
}
