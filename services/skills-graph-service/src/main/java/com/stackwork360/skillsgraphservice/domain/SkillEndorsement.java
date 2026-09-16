package com.stackwork360.skillsgraphservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record SkillEndorsement(
        UUID id,
        String tenantId,
        String workerId,
        String skillCode,
        String endorsedBy,
        ProficiencyLevel level,
        String note,
        Instant endorsedAt
) {
    public SkillEndorsement {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        workerId = requireText(workerId, "worker id is required");
        skillCode = requireText(skillCode, "skill code is required");
        endorsedBy = requireText(endorsedBy, "endorsed by is required");
        level = Objects.requireNonNull(level, "proficiency level is required");
        note = requireText(note, "endorsement note is required");
        endorsedAt = endorsedAt == null ? Instant.now() : endorsedAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
