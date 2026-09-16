package com.stackwork360.accessgovernanceservice.domain;

import java.util.Objects;
import java.util.UUID;

public record AccessApprovalPolicy(
        UUID id,
        String tenantId,
        ResourceType resourceType,
        AccessLevel minimumLevel,
        boolean managerApprovalRequired,
        boolean ownerApprovalRequired,
        boolean securityApprovalRequired
) {
    public AccessApprovalPolicy {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        resourceType = Objects.requireNonNull(resourceType, "resource type is required");
        minimumLevel = Objects.requireNonNull(minimumLevel, "minimum level is required");
        if (!managerApprovalRequired && !ownerApprovalRequired && !securityApprovalRequired) {
            throw new IllegalArgumentException("approval policy requires at least one approval gate");
        }
    }

    public boolean appliesTo(AccessResource resource, AccessLevel level) {
        return resource.type() == resourceType && level.ordinal() >= minimumLevel.ordinal();
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
