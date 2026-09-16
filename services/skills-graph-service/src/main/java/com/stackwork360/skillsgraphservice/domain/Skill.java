package com.stackwork360.skillsgraphservice.domain;

import java.util.Objects;
import java.util.UUID;

public record Skill(
        UUID id,
        String tenantId,
        String code,
        String name,
        SkillCategory category,
        String parentSkillCode,
        boolean active
) {
    public Skill {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        code = requireText(code, "skill code is required");
        name = requireText(name, "skill name is required");
        category = Objects.requireNonNull(category, "skill category is required");
        parentSkillCode = parentSkillCode == null || parentSkillCode.isBlank() ? null : parentSkillCode.trim();
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
