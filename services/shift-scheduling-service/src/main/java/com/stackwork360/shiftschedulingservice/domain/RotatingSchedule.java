package com.stackwork360.shiftschedulingservice.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record RotatingSchedule(
        UUID id,
        String tenantId,
        UUID templateId,
        List<String> workerRotation,
        LocalDate startsOn
) {
    public RotatingSchedule {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        Objects.requireNonNull(templateId, "template id is required");
        workerRotation = List.copyOf(workerRotation == null ? List.of() : workerRotation);
        if (workerRotation.isEmpty()) {
            throw new IllegalArgumentException("worker rotation is required");
        }
        Objects.requireNonNull(startsOn, "starts on is required");
    }

    public String workerFor(LocalDate date) {
        long days = java.time.temporal.ChronoUnit.DAYS.between(startsOn, date);
        int index = Math.floorMod((int) days, workerRotation.size());
        return workerRotation.get(index);
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
