package com.stackwork360.helpdeskservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.stackwork360.helpdeskservice.domain.Ticket;
import com.stackwork360.helpdeskservice.domain.TicketCategory;
import com.stackwork360.helpdeskservice.domain.TicketPriority;
import com.stackwork360.helpdeskservice.domain.TicketStatus;
import com.stackwork360.helpdeskservice.infrastructure.InMemoryTicketRepository;
import org.junit.jupiter.api.Test;

class HelpdeskApplicationServiceTest {
    private final HelpdeskApplicationService service = new HelpdeskApplicationService(new InMemoryTicketRepository());

    @Test
    void createsAndListsTicket() {
        Ticket ticket = service.create(command());

        assertEquals(ticket.id(), service.list("tenant-1").get(0).id());
        assertEquals(ticket.id(), service.requesterTickets("tenant-1", "worker-1").get(0).id());
    }

    @Test
    void queuesTicketByAssigneeGroup() {
        Ticket ticket = service.create(new CreateTicketCommand(
                "tenant-1",
                "worker-1",
                "Access issue",
                "Need GitHub access",
                TicketCategory.ACCESS,
                TicketPriority.NORMAL
        ));

        assertEquals(ticket.id(), service.queue("tenant-1", "SECURITY_ADMIN").get(0).id());
    }

    @Test
    void resolvesAndClosesTicket() {
        Ticket ticket = service.create(command());

        service.resolve(ticket.id(), "agent-1", "Done");
        Ticket closed = service.close(ticket.id());

        assertEquals(TicketStatus.CLOSED, closed.status());
    }

    private static CreateTicketCommand command() {
        return new CreateTicketCommand(
                "tenant-1",
                "worker-1",
                "Payroll question",
                "Payslip is missing",
                TicketCategory.PAYROLL,
                TicketPriority.HIGH
        );
    }
}
