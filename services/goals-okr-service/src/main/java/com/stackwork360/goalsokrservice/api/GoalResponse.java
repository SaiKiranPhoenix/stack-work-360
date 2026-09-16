package com.stackwork360.goalsokrservice.api;

import com.stackwork360.goalsokrservice.domain.Goal;
import com.stackwork360.goalsokrservice.domain.GoalLevel;
import com.stackwork360.goalsokrservice.domain.GoalStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record GoalResponse(
        UUID id,
        String tenantId,
        String ownerId,
        String teamId,
        GoalLevel level,
        String title,
        UUID parentGoalId,
        LocalDate startsOn,
        LocalDate dueOn,
        GoalStatus status,
        BigDecimal progress,
        List<ObjectiveResponse> objectives
) {
    static GoalResponse from(Goal goal) {
        return new GoalResponse(
                goal.id(),
                goal.tenantId(),
                goal.ownerId(),
                goal.teamId(),
                goal.level(),
                goal.title(),
                goal.parentGoalId(),
                goal.startsOn(),
                goal.dueOn(),
                goal.status(),
                goal.progress().value(),
                goal.objectives().stream().map(ObjectiveResponse::from).toList()
        );
    }
}
