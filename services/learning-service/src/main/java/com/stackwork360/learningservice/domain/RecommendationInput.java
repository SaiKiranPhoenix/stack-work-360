package com.stackwork360.learningservice.domain;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record RecommendationInput(
        UUID id,
        String tenantId,
        String workerId,
        String roleTarget,
        List<String> skillGaps,
        Instant capturedAt
) {
    public RecommendationInput {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        workerId = requireText(workerId, "worker id is required");
        roleTarget = requireText(roleTarget, "role target is required");
        skillGaps = List.copyOf(Objects.requireNonNull(skillGaps, "skill gaps are required"));
        if (skillGaps.isEmpty()) {
            throw new IllegalArgumentException("recommendation input requires at least one skill gap");
        }
        capturedAt = capturedAt == null ? Instant.now() : capturedAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
