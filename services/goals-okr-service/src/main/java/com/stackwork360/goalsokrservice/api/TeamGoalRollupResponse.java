package com.stackwork360.goalsokrservice.api;

import com.stackwork360.goalsokrservice.domain.TeamGoalRollup;
import java.math.BigDecimal;

public record TeamGoalRollupResponse(
        String tenantId,
        String teamId,
        int goalCount,
        int completedCount,
        int atRiskCount,
        BigDecimal averageProgress
) {
    static TeamGoalRollupResponse from(TeamGoalRollup rollup) {
        return new TeamGoalRollupResponse(rollup.tenantId(), rollup.teamId(), rollup.goalCount(), rollup.completedCount(), rollup.atRiskCount(), rollup.averageProgress());
    }
}
