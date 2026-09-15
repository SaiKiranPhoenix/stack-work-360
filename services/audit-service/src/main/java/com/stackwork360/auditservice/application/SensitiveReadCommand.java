package com.stackwork360.auditservice.application;

import java.util.Map;

public record SensitiveReadCommand(
        String tenantId,
        String actorId,
        String serviceName,
        String resourceType,
        String resourceId,
        String reason,
        String correlationId,
        Map<String, String> metadata
) {
}
