package com.stackwork360.skillsgraphservice.domain;

import java.util.Objects;

public record RequiredSkill(String skillCode, ProficiencyLevel requiredLevel) {
    public RequiredSkill {
        skillCode = requireText(skillCode, "skill code is required");
        requiredLevel = Objects.requireNonNull(requiredLevel, "required level is required");
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
