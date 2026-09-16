package com.stackwork360.workforceplanningservice.application;

import java.math.BigDecimal;

public record CreateHeadcountPlanCommand(
        String tenantId,
        String teamId,
        String location,
        int currentFullTime,
        int currentContractors,
        int targetFullTime,
        int targetContractors,
        BigDecimal averageFullTimeCost,
        BigDecimal averageContractorCost
) {
}
