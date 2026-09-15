package com.stackwork360.helpdeskservice.application;

import com.stackwork360.helpdeskservice.domain.Ticket;
import com.stackwork360.helpdeskservice.domain.TicketComment;
import com.stackwork360.helpdeskservice.domain.TicketRepository;
import com.stackwork360.web.ResourceNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class HelpdeskApplicationService {
    private final TicketRepository ticketRepository;

    public HelpdeskApplicationService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket create(CreateTicketCommand command) {
        Ticket ticket = Ticket.create(
                command.tenantId(),
                command.requesterId(),
                command.subject(),
                command.description(),
                command.category(),
                command.priority()
        );
        return ticketRepository.save(ticket);
    }

    public Ticket update(UUID ticketId, UpdateTicketCommand command) {
        Ticket ticket = get(ticketId);
        ticket.update(command.subject(), command.description(), command.category(), command.priority());
        return ticketRepository.save(ticket);
    }

    public Ticket assign(UUID ticketId, String assigneeId) {
        Ticket ticket = get(ticketId);
        ticket.assignTo(assigneeId);
        return ticketRepository.save(ticket);
    }

    public Ticket addComment(UUID ticketId, AddCommentCommand command) {
        Ticket ticket = get(ticketId);
        ticket.addComment(new TicketComment(
                null,
                command.authorId(),
                command.body(),
                command.internal(),
                command.attachments(),
                null
        ));
        return ticketRepository.save(ticket);
    }

    public Ticket escalate(UUID ticketId, String actorId, String reason) {
        Ticket ticket = get(ticketId);
        ticket.escalate(actorId, reason);
        return ticketRepository.save(ticket);
    }

    public Ticket resolve(UUID ticketId, String actorId, String resolution) {
        Ticket ticket = get(ticketId);
        ticket.resolve(actorId, resolution);
        return ticketRepository.save(ticket);
    }

    public Ticket close(UUID ticketId) {
        Ticket ticket = get(ticketId);
        ticket.close();
        return ticketRepository.save(ticket);
    }

    public Ticket get(UUID ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("ticket not found"));
    }

    public List<Ticket> list(String tenantId) {
        return ticketRepository.findByTenantId(tenantId);
    }

    public List<Ticket> requesterTickets(String tenantId, String requesterId) {
        return ticketRepository.findByTenantIdAndRequesterId(tenantId, requesterId);
    }

    public List<Ticket> queue(String tenantId, String assigneeGroup) {
        return ticketRepository.findByTenantIdAndAssigneeGroup(tenantId, assigneeGroup);
    }

    public List<Ticket> overdue(String tenantId) {
        Instant now = Instant.now();
        return ticketRepository.findByTenantId(tenantId).stream()
                .filter(ticket -> ticket.overdueAt(now))
                .toList();
    }
}
