package com.stackwork360.speakupcaseservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record SensitiveAuditRecord(
        UUID id,
        String tenantId,
        UUID caseId,
        String actorId,
        SensitiveAuditAction action,
        String reason,
        Instant occurredAt
) {
    public SensitiveAuditRecord {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        caseId = Objects.requireNonNull(caseId, "case id is required");
        actorId = requireText(actorId, "actor id is required");
        action = Objects.requireNonNull(action, "audit action is required");
        reason = requireText(reason, "reason is required");
        occurredAt = occurredAt == null ? Instant.now() : occurredAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
