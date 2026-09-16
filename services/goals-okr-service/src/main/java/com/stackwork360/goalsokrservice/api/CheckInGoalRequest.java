package com.stackwork360.goalsokrservice.api;

import com.stackwork360.goalsokrservice.application.CheckInGoalCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record CheckInGoalRequest(
        @NotNull UUID keyResultId,
        @NotNull BigDecimal value,
        @NotBlank String note,
        @NotBlank String checkedInBy
) {
    CheckInGoalCommand toCommand(String tenantId, UUID goalId) {
        return new CheckInGoalCommand(tenantId, goalId, keyResultId, value, note, checkedInBy);
    }
}
