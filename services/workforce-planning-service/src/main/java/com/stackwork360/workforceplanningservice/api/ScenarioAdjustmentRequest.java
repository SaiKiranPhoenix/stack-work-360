package com.stackwork360.workforceplanningservice.api;

import com.stackwork360.workforceplanningservice.domain.ScenarioAdjustment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ScenarioAdjustmentRequest(
        @NotBlank String teamId,
        @NotBlank String location,
        int fullTimeDelta,
        int contractorDelta,
        @NotNull BigDecimal annualCostDelta
) {
    ScenarioAdjustment toDomain() {
        return new ScenarioAdjustment(teamId, location, fullTimeDelta, contractorDelta, annualCostDelta);
    }
}
