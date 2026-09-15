package com.stackwork360.helpdeskservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository {
    Ticket save(Ticket ticket);

    Optional<Ticket> findById(UUID id);

    List<Ticket> findByTenantId(String tenantId);

    List<Ticket> findByTenantIdAndRequesterId(String tenantId, String requesterId);

    List<Ticket> findByTenantIdAndAssigneeGroup(String tenantId, String assigneeGroup);
}
