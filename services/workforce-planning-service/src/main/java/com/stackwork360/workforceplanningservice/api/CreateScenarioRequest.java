package com.stackwork360.workforceplanningservice.api;

import com.stackwork360.workforceplanningservice.application.CreateScenarioCommand;
import com.stackwork360.workforceplanningservice.domain.ScenarioType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateScenarioRequest(
        @NotBlank String name,
        @NotNull ScenarioType type,
        @Valid @NotEmpty List<ScenarioAdjustmentRequest> adjustments
) {
    CreateScenarioCommand toCommand(String tenantId) {
        return new CreateScenarioCommand(tenantId, name, type, adjustments.stream().map(ScenarioAdjustmentRequest::toDomain).toList());
    }
}
