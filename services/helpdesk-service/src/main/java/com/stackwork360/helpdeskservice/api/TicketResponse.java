package com.stackwork360.helpdeskservice.api;

import com.stackwork360.helpdeskservice.domain.Ticket;
import com.stackwork360.helpdeskservice.domain.TicketCategory;
import com.stackwork360.helpdeskservice.domain.TicketPriority;
import com.stackwork360.helpdeskservice.domain.TicketStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record TicketResponse(
        UUID id,
        String requesterId,
        String subject,
        String description,
        TicketCategory category,
        TicketPriority priority,
        TicketStatus status,
        String assigneeGroup,
        String assigneeId,
        Instant dueAt,
        List<TicketCommentResponse> comments,
        Instant createdAt,
        Instant updatedAt
) {
    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(
                ticket.id(),
                ticket.requesterId(),
                ticket.subject(),
                ticket.description(),
                ticket.category(),
                ticket.priority(),
                ticket.status(),
                ticket.assigneeGroup(),
                ticket.assigneeId(),
                ticket.dueAt(),
                ticket.comments().stream().map(TicketCommentResponse::from).toList(),
                ticket.createdAt(),
                ticket.updatedAt()
        );
    }
}
