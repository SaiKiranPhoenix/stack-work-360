package com.stackwork360.skillsgraphservice.domain;

public record SkillGap(
        String skillCode,
        ProficiencyLevel requiredLevel,
        ProficiencyLevel currentLevel,
        boolean missing
) {
}
