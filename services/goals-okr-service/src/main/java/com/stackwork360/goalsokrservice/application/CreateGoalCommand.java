package com.stackwork360.goalsokrservice.application;

import com.stackwork360.goalsokrservice.domain.GoalLevel;
import com.stackwork360.goalsokrservice.domain.Objective;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateGoalCommand(
        String tenantId,
        String ownerId,
        String teamId,
        GoalLevel level,
        String title,
        UUID parentGoalId,
        LocalDate startsOn,
        LocalDate dueOn,
        List<Objective> objectives
) {
}
