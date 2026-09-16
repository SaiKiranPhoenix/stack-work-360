package com.stackwork360.goalsokrservice.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record GoalAlignmentNode(
        UUID goalId,
        UUID parentGoalId,
        String title,
        String ownerId,
        String teamId,
        GoalLevel level,
        BigDecimal progress
) {
    public static GoalAlignmentNode from(Goal goal) {
        return new GoalAlignmentNode(goal.id(), goal.parentGoalId(), goal.title(), goal.ownerId(), goal.teamId(), goal.level(), goal.progress().value());
    }
}
