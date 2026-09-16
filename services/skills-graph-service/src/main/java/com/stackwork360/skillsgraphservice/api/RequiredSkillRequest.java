package com.stackwork360.skillsgraphservice.api;

import com.stackwork360.skillsgraphservice.domain.ProficiencyLevel;
import com.stackwork360.skillsgraphservice.domain.RequiredSkill;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequiredSkillRequest(@NotBlank String skillCode, @NotNull ProficiencyLevel requiredLevel) {
    RequiredSkill toDomain() {
        return new RequiredSkill(skillCode, requiredLevel);
    }
}
