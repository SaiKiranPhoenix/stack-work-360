package com.stackwork360.talentmarketplaceservice.api;

import com.stackwork360.talentmarketplaceservice.domain.CandidateSkill;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CandidateSkillRequest(@NotBlank String skillCode, @Min(1) @Max(5) int level) {
    CandidateSkill toDomain() {
        return new CandidateSkill(skillCode, level);
    }
}
