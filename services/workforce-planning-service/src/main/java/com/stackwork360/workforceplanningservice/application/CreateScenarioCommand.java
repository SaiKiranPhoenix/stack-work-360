package com.stackwork360.workforceplanningservice.application;

import com.stackwork360.workforceplanningservice.domain.ScenarioAdjustment;
import com.stackwork360.workforceplanningservice.domain.ScenarioType;
import java.util.List;

public record CreateScenarioCommand(
        String tenantId,
        String name,
        ScenarioType type,
        List<ScenarioAdjustment> adjustments
) {
}
