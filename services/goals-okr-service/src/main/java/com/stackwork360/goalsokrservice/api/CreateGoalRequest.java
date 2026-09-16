package com.stackwork360.goalsokrservice.api;

import com.stackwork360.goalsokrservice.application.CreateGoalCommand;
import com.stackwork360.goalsokrservice.domain.GoalLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateGoalRequest(
        @NotBlank String ownerId,
        @NotBlank String teamId,
        @NotNull GoalLevel level,
        @NotBlank String title,
        UUID parentGoalId,
        @NotNull LocalDate startsOn,
        @NotNull LocalDate dueOn,
        @Valid @NotEmpty List<ObjectiveRequest> objectives
) {
    CreateGoalCommand toCommand(String tenantId) {
        return new CreateGoalCommand(tenantId, ownerId, teamId, level, title, parentGoalId, startsOn, dueOn, objectives.stream().map(ObjectiveRequest::toDomain).toList());
    }
}
