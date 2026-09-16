package com.stackwork360.goalsokrservice.domain;

import java.math.BigDecimal;

public record TeamGoalRollup(
        String tenantId,
        String teamId,
        int goalCount,
        int completedCount,
        int atRiskCount,
        BigDecimal averageProgress
) {
}
