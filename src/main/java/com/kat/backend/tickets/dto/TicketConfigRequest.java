package com.kat.backend.tickets.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TicketConfigRequest {

    @NotNull
    private Boolean enabled;

    private String panelChannelId;

    private String panelMessageId;

    private String categoryId;

    @NotBlank
    @Size(max = 80)
    private String buttonLabel = "Create Ticket";

    @NotBlank
    private String buttonStyle = "PRIMARY";

    @NotBlank
    @Size(max = 256)
    private String embedTitle = "Support Tickets";

    @Size(max = 2000)
    private String embedDescription = "Click the button below to create a support ticket.";

    @Size(max = 50)
    private String embedColor = "#5865F2";

    @NotNull
    private Boolean transcriptEnabled = false;

    @NotNull
    private Boolean allowUserToClose = true;

    @Min(1)
    @Max(10)
    private int maxOpenTicketsPerUser = 1;

    @NotBlank
    @Size(max = 100)
    private String ticketChannelNameTemplate = "ticket-{username}";

    @Size(max = 2000)
    private String welcomeMessage;

    @Size(max = 20)
    private List<String> supportRoleIds = new ArrayList<>();
}
