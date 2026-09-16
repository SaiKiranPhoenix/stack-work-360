package com.stackwork360.skillsgraphservice.domain;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record CertificationSkillMapping(
        UUID id,
        String tenantId,
        String certificationCode,
        List<String> skillCodes
) {
    public CertificationSkillMapping {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        certificationCode = requireText(certificationCode, "certification code is required");
        skillCodes = List.copyOf(Objects.requireNonNull(skillCodes, "skill codes are required"));
        if (skillCodes.isEmpty()) {
            throw new IllegalArgumentException("certification mapping requires at least one skill");
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
