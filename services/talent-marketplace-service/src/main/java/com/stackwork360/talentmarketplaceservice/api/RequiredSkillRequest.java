package com.stackwork360.talentmarketplaceservice.api;

import com.stackwork360.talentmarketplaceservice.domain.RequiredSkill;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RequiredSkillRequest(@NotBlank String skillCode, @Min(1) @Max(5) int minimumLevel) {
    RequiredSkill toDomain() {
        return new RequiredSkill(skillCode, minimumLevel);
    }
}
