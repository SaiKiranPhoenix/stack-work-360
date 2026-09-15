package com.stackwork360.events;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record DomainEventEnvelope<T>(
        UUID eventId,
        String eventType,
        int eventVersion,
        Instant occurredAt,
        String producer,
        String tenantId,
        String actorId,
        String correlationId,
        String traceId,
        T payload,
        Map<String, String> metadata
) {
}
