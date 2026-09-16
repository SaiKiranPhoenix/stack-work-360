package com.stackwork360.accessgovernanceservice.domain;

import java.util.Objects;
import java.util.UUID;

public record AccessResource(
        UUID id,
        String tenantId,
        String resourceCode,
        String name,
        ResourceType type,
        boolean privileged,
        String ownerId
) {
    public AccessResource {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        resourceCode = requireText(resourceCode, "resource code is required");
        name = requireText(name, "resource name is required");
        type = Objects.requireNonNull(type, "resource type is required");
        ownerId = requireText(ownerId, "owner id is required");
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
