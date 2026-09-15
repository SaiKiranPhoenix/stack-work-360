package com.stackwork360.auditservice.application;

import com.stackwork360.auditservice.domain.AuditAction;
import com.stackwork360.auditservice.domain.AuditOutcome;
import com.stackwork360.auditservice.domain.AuditSensitivity;
import java.util.Map;

public record RecordAuditCommand(
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
        Map<String, String> metadata
) {
}
