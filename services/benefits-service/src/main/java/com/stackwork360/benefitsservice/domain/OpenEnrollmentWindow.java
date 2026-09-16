package com.stackwork360.benefitsservice.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record OpenEnrollmentWindow(
        UUID id,
        String tenantId,
        String name,
        LocalDate startsOn,
        LocalDate endsOn
) {
    public OpenEnrollmentWindow {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        name = requireText(name, "window name is required");
        startsOn = Objects.requireNonNull(startsOn, "start date is required");
        endsOn = Objects.requireNonNull(endsOn, "end date is required");
        if (endsOn.isBefore(startsOn)) {
            throw new IllegalArgumentException("open enrollment end date cannot be before start date");
        }
    }

    public boolean activeOn(LocalDate date) {
        return !date.isBefore(startsOn) && !date.isAfter(endsOn);
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
