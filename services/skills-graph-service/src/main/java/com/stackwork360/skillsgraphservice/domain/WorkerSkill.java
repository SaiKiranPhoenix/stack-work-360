package com.stackwork360.skillsgraphservice.domain;

import java.time.Instant;
import java.util.Objects;

public record WorkerSkill(
        String skillCode,
        ProficiencyLevel level,
        Instant updatedAt
) {
    public WorkerSkill {
        skillCode = requireText(skillCode, "skill code is required");
        level = Objects.requireNonNull(level, "proficiency level is required");
        updatedAt = updatedAt == null ? Instant.now() : updatedAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
