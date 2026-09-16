package com.stackwork360.skillsgraphservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record SkillEvidence(
        UUID id,
        String tenantId,
        String workerId,
        String skillCode,
        EvidenceType type,
        String sourceId,
        ProficiencyLevel level,
        Instant capturedAt
) {
    public SkillEvidence {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        workerId = requireText(workerId, "worker id is required");
        skillCode = requireText(skillCode, "skill code is required");
        type = Objects.requireNonNull(type, "evidence type is required");
        sourceId = requireText(sourceId, "source id is required");
        level = Objects.requireNonNull(level, "proficiency level is required");
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
