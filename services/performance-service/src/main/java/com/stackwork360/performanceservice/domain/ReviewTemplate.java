package com.stackwork360.performanceservice.domain;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record ReviewTemplate(
        UUID id,
        String tenantId,
        String name,
        ReviewType type,
        List<String> competencies,
        boolean active
) {
    public ReviewTemplate {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        name = requireText(name, "template name is required");
        type = Objects.requireNonNull(type, "review type is required");
        competencies = List.copyOf(Objects.requireNonNull(competencies, "competencies are required"));
        if (competencies.isEmpty()) {
            throw new IllegalArgumentException("template requires at least one competency");
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
