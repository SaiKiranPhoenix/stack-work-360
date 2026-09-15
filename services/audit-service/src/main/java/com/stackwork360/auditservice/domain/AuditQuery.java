package com.stackwork360.auditservice.domain;

import java.time.Instant;

public record AuditQuery(
        String tenantId,
        String actorId,
        String serviceName,
        String resourceType,
        AuditAction action,
        AuditSensitivity sensitivity,
        Instant from,
        Instant to
) {
}
