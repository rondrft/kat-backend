package com.kat.backend.tickets.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ticket_config")
@Getter
@Setter
@NoArgsConstructor
public class TicketConfig {

    @Id
    @Column(name = "guild_id")
    private String guildId;

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(name = "panel_channel_id")
    private String panelChannelId;

    @Column(name = "panel_message_id")
    private String panelMessageId;

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "button_label", nullable = false)
    private String buttonLabel = "Create Ticket";

    @Column(name = "button_style", nullable = false)
    private String buttonStyle = "PRIMARY";

    @Column(name = "embed_title", nullable = false)
    private String embedTitle = "Support Tickets";

    @Column(name = "embed_description", nullable = false, length = 2000)
    private String embedDescription = "Click the button below to create a support ticket.";

    @Column(name = "embed_color", nullable = false)
    private String embedColor = "#5865F2";

    @Column(name = "transcript_enabled", nullable = false)
    private boolean transcriptEnabled = false;

    @Column(name = "allow_user_to_close", nullable = false)
    private boolean allowUserToClose = true;

    @Column(name = "max_open_tickets_per_user", nullable = false)
    private int maxOpenTicketsPerUser = 1;

    @Column(name = "ticket_channel_name_template", nullable = false)
    private String ticketChannelNameTemplate = "ticket-{username}";

    @Column(name = "welcome_message", length = 2000)
    private String welcomeMessage;

    @ElementCollection
    @CollectionTable(name = "ticket_support_roles", joinColumns = @JoinColumn(name = "guild_id"))
    @Column(name = "role_id")
    private List<String> supportRoleIds = new ArrayList<>();
}
