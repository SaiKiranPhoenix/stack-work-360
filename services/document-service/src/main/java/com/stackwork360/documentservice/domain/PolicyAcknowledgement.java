package com.stackwork360.documentservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record PolicyAcknowledgement(
        UUID id,
        String tenantId,
        UUID documentId,
        String workerId,
        Instant acknowledgedAt,
        String ipAddress
) {
    public PolicyAcknowledgement {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        Objects.requireNonNull(documentId, "document id is required");
        workerId = requireText(workerId, "worker id is required");
        acknowledgedAt = acknowledgedAt == null ? Instant.now() : acknowledgedAt;
        ipAddress = ipAddress == null ? "" : ipAddress.trim();
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
