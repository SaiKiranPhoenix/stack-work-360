package com.stackwork360.workforceplanningservice.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record WorkforceCostSimulation(
        String tenantId,
        UUID scenarioId,
        int headcountDelta,
        BigDecimal annualCostDelta,
        List<ScenarioAdjustment> adjustments
) {
}
