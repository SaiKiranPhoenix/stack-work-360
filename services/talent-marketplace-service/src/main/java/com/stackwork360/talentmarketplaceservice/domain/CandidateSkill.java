package com.stackwork360.talentmarketplaceservice.domain;

import java.util.Objects;

public record CandidateSkill(
        String skillCode,
        int level
) {
    public CandidateSkill {
        skillCode = requireText(skillCode, "skill code is required");
        if (level < 1 || level > 5) {
            throw new IllegalArgumentException("skill level must be between 1 and 5");
        }
    }

    public boolean satisfies(RequiredSkill required) {
        return skillCode.equalsIgnoreCase(required.skillCode()) && level >= required.minimumLevel();
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
