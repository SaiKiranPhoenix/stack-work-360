package com.stackwork360.documentservice.application;

import java.util.UUID;

public record AcknowledgePolicyCommand(
        String tenantId,
        UUID documentId,
        String workerId,
        String ipAddress
) {
}
