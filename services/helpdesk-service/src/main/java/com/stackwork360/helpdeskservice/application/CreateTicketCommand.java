package com.stackwork360.helpdeskservice.application;

import com.stackwork360.helpdeskservice.domain.TicketCategory;
import com.stackwork360.helpdeskservice.domain.TicketPriority;

public record CreateTicketCommand(
        String tenantId,
        String requesterId,
        String subject,
        String description,
        TicketCategory category,
        TicketPriority priority
) {
}
