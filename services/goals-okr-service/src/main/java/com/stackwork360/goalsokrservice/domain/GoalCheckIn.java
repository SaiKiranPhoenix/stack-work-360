package com.stackwork360.goalsokrservice.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record GoalCheckIn(
        UUID id,
        String tenantId,
        UUID goalId,
        UUID keyResultId,
        BigDecimal value,
        String note,
        String checkedInBy,
        Instant checkedInAt
) {
    public GoalCheckIn {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        goalId = Objects.requireNonNull(goalId, "goal id is required");
        keyResultId = Objects.requireNonNull(keyResultId, "key result id is required");
        value = Objects.requireNonNull(value, "check-in value is required");
        note = requireText(note, "check-in note is required");
        checkedInBy = requireText(checkedInBy, "checked-in by is required");
        checkedInAt = checkedInAt == null ? Instant.now() : checkedInAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
