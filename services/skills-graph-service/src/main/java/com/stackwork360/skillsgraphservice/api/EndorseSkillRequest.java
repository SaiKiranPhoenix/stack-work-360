package com.stackwork360.skillsgraphservice.api;

import com.stackwork360.skillsgraphservice.application.EndorseSkillCommand;
import com.stackwork360.skillsgraphservice.domain.ProficiencyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EndorseSkillRequest(
        @NotBlank String workerId,
        @NotBlank String skillCode,
        @NotBlank String endorsedBy,
        @NotNull ProficiencyLevel level,
        @NotBlank String note
) {
    EndorseSkillCommand toCommand(String tenantId) {
        return new EndorseSkillCommand(tenantId, workerId, skillCode, endorsedBy, level, note);
    }
}
