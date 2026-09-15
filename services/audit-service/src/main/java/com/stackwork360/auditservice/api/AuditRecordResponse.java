package com.stackwork360.auditservice.api;

import com.stackwork360.auditservice.domain.AuditAction;
import com.stackwork360.auditservice.domain.AuditOutcome;
import com.stackwork360.auditservice.domain.AuditRecord;
import com.stackwork360.auditservice.domain.AuditSensitivity;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record AuditRecordResponse(
        UUID id,
        String tenantId,
        String actorId,
        String serviceName,
        String resourceType,
        String resourceId,
        AuditAction action,
        AuditOutcome outcome,
        AuditSensitivity sensitivity,
        String reason,
        String correlationId,
        Map<String, String> metadata,
        Instant occurredAt
) {
    static AuditRecordResponse from(AuditRecord record) {
        return new AuditRecordResponse(
                record.id(),
                record.tenantId(),
                record.actorId(),
                record.serviceName(),
                record.resourceType(),
                record.resourceId(),
                record.action(),
                record.outcome(),
                record.sensitivity(),
                record.reason(),
                record.correlationId(),
                record.metadata(),
                record.occurredAt()
        );
    }
}
