package com.stackwork360.skillsgraphservice.api;

import com.stackwork360.skillsgraphservice.application.UpsertWorkerSkillCommand;
import com.stackwork360.skillsgraphservice.domain.ProficiencyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpsertWorkerSkillRequest(
        @NotBlank String workerId,
        @NotBlank String skillCode,
        @NotNull ProficiencyLevel level
) {
    UpsertWorkerSkillCommand toCommand(String tenantId) {
        return new UpsertWorkerSkillCommand(tenantId, workerId, skillCode, level);
    }
}
