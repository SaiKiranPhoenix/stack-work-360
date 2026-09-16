package com.stackwork360.skillsgraphservice.domain;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record ProjectSkillMapping(
        UUID id,
        String tenantId,
        String projectCode,
        List<String> skillCodes
) {
    public ProjectSkillMapping {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        projectCode = requireText(projectCode, "project code is required");
        skillCodes = List.copyOf(Objects.requireNonNull(skillCodes, "skill codes are required"));
        if (skillCodes.isEmpty()) {
            throw new IllegalArgumentException("project mapping requires at least one skill");
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
