package com.stackwork360.helpdeskservice.infrastructure;

import com.stackwork360.helpdeskservice.domain.Ticket;
import com.stackwork360.helpdeskservice.domain.TicketRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryTicketRepository implements TicketRepository {
    private final ConcurrentMap<UUID, Ticket> ticketsById = new ConcurrentHashMap<>();

    @Override
    public Ticket save(Ticket ticket) {
        ticketsById.put(ticket.id(), ticket);
        return ticket;
    }

    @Override
    public Optional<Ticket> findById(UUID id) {
        return Optional.ofNullable(ticketsById.get(id));
    }

    @Override
    public List<Ticket> findByTenantId(String tenantId) {
        return ticketsById.values().stream()
                .filter(ticket -> ticket.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(Ticket::createdAt))
                .toList();
    }

    @Override
    public List<Ticket> findByTenantIdAndRequesterId(String tenantId, String requesterId) {
        return ticketsById.values().stream()
                .filter(ticket -> ticket.tenantId().equals(tenantId))
                .filter(ticket -> ticket.requesterId().equals(requesterId))
                .sorted(Comparator.comparing(Ticket::createdAt))
                .toList();
    }

    @Override
    public List<Ticket> findByTenantIdAndAssigneeGroup(String tenantId, String assigneeGroup) {
        return ticketsById.values().stream()
                .filter(ticket -> ticket.tenantId().equals(tenantId))
                .filter(ticket -> ticket.assigneeGroup().equals(assigneeGroup))
                .sorted(Comparator.comparing(Ticket::dueAt))
                .toList();
    }
}
