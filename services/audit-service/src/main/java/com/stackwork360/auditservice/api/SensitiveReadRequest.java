package com.stackwork360.auditservice.api;

import com.stackwork360.auditservice.application.SensitiveReadCommand;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public record SensitiveReadRequest(
        @NotBlank String actorId,
        @NotBlank String serviceName,
        @NotBlank String resourceType,
        @NotBlank String resourceId,
        @NotBlank String reason,
        @NotBlank String correlationId,
        Map<String, String> metadata
) {
    SensitiveReadCommand toCommand(String tenantId) {
        return new SensitiveReadCommand(
                tenantId,
                actorId,
                serviceName,
                resourceType,
                resourceId,
                reason,
                correlationId,
                metadata == null ? Map.of() : metadata
        );
    }
}
