package com.stackwork360.helpdeskservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class TicketTest {
    @Test
    void routesTicketByCategoryAndSetsSla() {
        Ticket ticket = ticket(TicketCategory.PAYROLL, TicketPriority.HIGH);

        assertEquals("PAYROLL_OPS", ticket.assigneeGroup());
        assertEquals(TicketStatus.OPEN, ticket.status());
    }

    @Test
    void assignmentMovesTicketIntoProgress() {
        Ticket ticket = ticket(TicketCategory.HR, TicketPriority.NORMAL);

        ticket.assignTo("agent-1");

        assertEquals("agent-1", ticket.assigneeId());
        assertEquals(TicketStatus.IN_PROGRESS, ticket.status());
    }

    @Test
    void commentSupportsAttachmentMetadata() {
        Ticket ticket = ticket(TicketCategory.HR, TicketPriority.NORMAL);

        ticket.addComment(new TicketComment(
                null,
                "agent-1",
                "Please see attachment",
                false,
                List.of(new AttachmentMetadata(null, "proof.pdf", "tickets/proof.pdf", "application/pdf", 1024)),
                null
        ));

        assertEquals(1, ticket.comments().size());
        assertEquals(1, ticket.comments().get(0).attachments().size());
    }

    @Test
    void escalationMarksTicketUrgentAndEscalated() {
        Ticket ticket = ticket(TicketCategory.GENERAL, TicketPriority.LOW);

        ticket.escalate("agent-1", "SLA risk");

        assertEquals(TicketStatus.ESCALATED, ticket.status());
        assertEquals(TicketPriority.URGENT, ticket.priority());
    }

    @Test
    void onlyResolvedTicketCanBeClosed() {
        Ticket ticket = ticket(TicketCategory.GENERAL, TicketPriority.NORMAL);

        assertThrows(IllegalStateException.class, ticket::close);
    }

    private static Ticket ticket(TicketCategory category, TicketPriority priority) {
        return Ticket.create(
                "tenant-1",
                "worker-1",
                "Need help",
                "Something needs attention",
                category,
                priority
        );
    }
}
