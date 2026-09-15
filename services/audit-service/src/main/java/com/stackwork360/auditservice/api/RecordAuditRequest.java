package com.stackwork360.auditservice.api;

import com.stackwork360.auditservice.application.RecordAuditCommand;
import com.stackwork360.auditservice.domain.AuditAction;
import com.stackwork360.auditservice.domain.AuditOutcome;
import com.stackwork360.auditservice.domain.AuditSensitivity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record RecordAuditRequest(
        @NotBlank String actorId,
        @NotBlank String serviceName,
        @NotBlank String resourceType,
        @NotBlank String resourceId,
        @NotNull AuditAction action,
        @NotNull AuditOutcome outcome,
        @NotNull AuditSensitivity sensitivity,
        @NotBlank String reason,
        @NotBlank String correlationId,
        Map<String, String> metadata
) {
    RecordAuditCommand toCommand(String tenantId) {
        return new RecordAuditCommand(
                tenantId,
                actorId,
                serviceName,
                resourceType,
                resourceId,
                action,
                outcome,
                sensitivity,
                reason,
                correlationId,
                metadata == null ? Map.of() : metadata
        );
    }
}
