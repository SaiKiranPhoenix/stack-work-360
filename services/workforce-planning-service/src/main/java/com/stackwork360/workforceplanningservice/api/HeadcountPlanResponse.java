package com.stackwork360.workforceplanningservice.api;

import com.stackwork360.workforceplanningservice.domain.HeadcountPlan;
import java.math.BigDecimal;
import java.util.UUID;

public record HeadcountPlanResponse(UUID id, String tenantId, String teamId, String location, int currentFullTime, int currentContractors, int targetFullTime, int targetContractors, BigDecimal averageFullTimeCost, BigDecimal averageContractorCost, int targetHeadcount, BigDecimal targetCost) {
    static HeadcountPlanResponse from(HeadcountPlan plan) {
        return new HeadcountPlanResponse(plan.id(), plan.tenantId(), plan.teamId(), plan.location(), plan.currentFullTime(), plan.currentContractors(), plan.targetFullTime(), plan.targetContractors(), plan.averageFullTimeCost(), plan.averageContractorCost(), plan.targetHeadcount(), plan.targetCost());
    }
}
