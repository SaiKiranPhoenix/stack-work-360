package com.stackwork360.goalsokrservice.application;

import java.math.BigDecimal;
import java.util.UUID;

public record CheckInGoalCommand(
        String tenantId,
        UUID goalId,
        UUID keyResultId,
        BigDecimal value,
        String note,
        String checkedInBy
) {
}
