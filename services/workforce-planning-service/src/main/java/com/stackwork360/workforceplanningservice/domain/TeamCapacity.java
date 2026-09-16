package com.stackwork360.workforceplanningservice.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record TeamCapacity(
        String tenantId,
        String teamId,
        int headcount,
        BigDecimal weeklyCapacityHours
) {
    public static TeamCapacity from(HeadcountPlan plan) {
        int headcount = plan.targetHeadcount();
        return new TeamCapacity(
                plan.tenantId(),
                plan.teamId(),
                headcount,
                BigDecimal.valueOf(headcount).multiply(new BigDecimal("32")).setScale(2, RoundingMode.HALF_UP)
        );
    }
}
