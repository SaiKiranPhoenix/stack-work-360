package com.stackwork360.learningservice.domain;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record LearningPath(
        UUID id,
        String tenantId,
        String title,
        String roleTarget,
        List<UUID> resourceIds,
        boolean active
) {
    public LearningPath {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        title = requireText(title, "path title is required");
        roleTarget = requireText(roleTarget, "role target is required");
        resourceIds = List.copyOf(Objects.requireNonNull(resourceIds, "resource ids are required"));
        if (resourceIds.isEmpty()) {
            throw new IllegalArgumentException("learning path requires at least one resource");
        }
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
