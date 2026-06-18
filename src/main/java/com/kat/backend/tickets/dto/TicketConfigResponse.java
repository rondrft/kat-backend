package com.kat.backend.tickets.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class TicketConfigResponse {

    private String guildId;
    private boolean enabled;
    private String panelChannelId;
    private String panelMessageId;
    private String categoryId;
    private String buttonLabel;
    private String buttonStyle;
    private String embedTitle;
    private String embedDescription;
    private String embedColor;
    private boolean transcriptEnabled;
    private boolean allowUserToClose;
    private int maxOpenTicketsPerUser;
    private String ticketChannelNameTemplate;
    private String welcomeMessage;
    private List<String> supportRoleIds;
}
