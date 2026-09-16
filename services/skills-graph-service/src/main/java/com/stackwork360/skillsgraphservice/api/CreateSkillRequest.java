package com.stackwork360.skillsgraphservice.api;

import com.stackwork360.skillsgraphservice.application.CreateSkillCommand;
import com.stackwork360.skillsgraphservice.domain.SkillCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSkillRequest(
        @NotBlank String code,
        @NotBlank String name,
        @NotNull SkillCategory category,
        String parentSkillCode,
        boolean active
) {
    CreateSkillCommand toCommand(String tenantId) {
        return new CreateSkillCommand(tenantId, code, name, category, parentSkillCode, active);
    }
}
