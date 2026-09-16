package com.stackwork360.talentmarketplaceservice.domain;

import java.util.Objects;

public record RequiredSkill(
        String skillCode,
        int minimumLevel
) {
    public RequiredSkill {
        skillCode = requireText(skillCode, "skill code is required");
        if (minimumLevel < 1 || minimumLevel > 5) {
            throw new IllegalArgumentException("minimum level must be between 1 and 5");
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
