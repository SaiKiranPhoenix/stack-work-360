package com.stackwork360.shiftschedulingservice.domain;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

public record ShiftTemplate(
        UUID id,
        String tenantId,
        String name,
        LocalTime startTime,
        LocalTime endTime,
        int requiredStaff
) {
    public ShiftTemplate {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        name = requireText(name, "shift template name is required");
        Objects.requireNonNull(startTime, "start time is required");
        Objects.requireNonNull(endTime, "end time is required");
        if (requiredStaff < 1) {
            throw new IllegalArgumentException("required staff must be positive");
        }
    }

    public Duration duration() {
        Duration duration = Duration.between(startTime, endTime);
        return duration.isNegative() || duration.isZero() ? duration.plusDays(1) : duration;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
