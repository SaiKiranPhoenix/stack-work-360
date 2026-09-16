package com.stackwork360.talentmarketplaceservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record MobilityHistory(
        UUID id,
        String tenantId,
        String workerId,
        UUID opportunityId,
        OpportunityType opportunityType,
        String title,
        Instant recordedAt
) {
    public MobilityHistory {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        workerId = requireText(workerId, "worker id is required");
        opportunityId = Objects.requireNonNull(opportunityId, "opportunity id is required");
        opportunityType = Objects.requireNonNull(opportunityType, "opportunity type is required");
        title = requireText(title, "title is required");
        recordedAt = recordedAt == null ? Instant.now() : recordedAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
