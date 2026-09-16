package com.stackwork360.goalsokrservice.api;

import com.stackwork360.goalsokrservice.domain.GoalCheckIn;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record GoalCheckInResponse(
        UUID id,
        String tenantId,
        UUID goalId,
        UUID keyResultId,
        BigDecimal value,
        String note,
        String checkedInBy,
        Instant checkedInAt
) {
    static GoalCheckInResponse from(GoalCheckIn checkIn) {
        return new GoalCheckInResponse(checkIn.id(), checkIn.tenantId(), checkIn.goalId(), checkIn.keyResultId(), checkIn.value(), checkIn.note(), checkIn.checkedInBy(), checkIn.checkedInAt());
    }
}
