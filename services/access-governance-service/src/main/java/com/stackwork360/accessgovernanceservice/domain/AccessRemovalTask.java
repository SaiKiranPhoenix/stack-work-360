package com.stackwork360.accessgovernanceservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record AccessRemovalTask(
        UUID id,
        String tenantId,
        UUID accessRequestId,
        String requesterId,
        String resourceCode,
        String reason,
        boolean completed,
        Instant createdAt
) {
    public AccessRemovalTask {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        accessRequestId = Objects.requireNonNull(accessRequestId, "access request id is required");
        requesterId = requireText(requesterId, "requester id is required");
        resourceCode = requireText(resourceCode, "resource code is required");
        reason = requireText(reason, "reason is required");
        createdAt = createdAt == null ? Instant.now() : createdAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
