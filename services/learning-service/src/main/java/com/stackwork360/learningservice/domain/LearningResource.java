package com.stackwork360.learningservice.domain;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record LearningResource(
        UUID id,
        String tenantId,
        String title,
        LearningResourceType type,
        String provider,
        int durationMinutes,
        List<String> skills,
        boolean active
) {
    public LearningResource {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        title = requireText(title, "resource title is required");
        type = Objects.requireNonNull(type, "resource type is required");
        provider = requireText(provider, "provider is required");
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("duration minutes must be positive");
        }
        skills = List.copyOf(Objects.requireNonNull(skills, "skills are required"));
        if (skills.isEmpty()) {
            throw new IllegalArgumentException("resource requires at least one skill");
        }
    }

    public boolean teachesAny(List<String> targetSkills) {
        return skills.stream().anyMatch(skill -> targetSkills.stream().anyMatch(target -> target.equalsIgnoreCase(skill)));
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
