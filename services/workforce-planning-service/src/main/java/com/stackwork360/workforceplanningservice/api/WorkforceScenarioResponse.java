package com.stackwork360.workforceplanningservice.api;

import com.stackwork360.workforceplanningservice.domain.ScenarioAdjustment;
import com.stackwork360.workforceplanningservice.domain.ScenarioType;
import com.stackwork360.workforceplanningservice.domain.WorkforceScenario;
import java.util.List;
import java.util.UUID;

public record WorkforceScenarioResponse(UUID id, String tenantId, String name, ScenarioType type, List<ScenarioAdjustment> adjustments) {
    static WorkforceScenarioResponse from(WorkforceScenario scenario) {
        return new WorkforceScenarioResponse(scenario.id(), scenario.tenantId(), scenario.name(), scenario.type(), scenario.adjustments());
    }
}
