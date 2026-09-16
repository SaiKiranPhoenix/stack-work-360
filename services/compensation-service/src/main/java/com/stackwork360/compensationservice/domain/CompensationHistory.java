package com.stackwork360.compensationservice.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record CompensationHistory(
        UUID id,
        String tenantId,
        String workerId,
        String jobLevel,
        String location,
        Money salary,
        LocalDate effectiveDate,
        String source,
        Instant recordedAt
) {
    public CompensationHistory {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        workerId = requireText(workerId, "worker id is required");
        jobLevel = requireText(jobLevel, "job level is required");
        location = requireText(location, "location is required");
        Objects.requireNonNull(salary, "salary is required");
        Objects.requireNonNull(effectiveDate, "effective date is required");
        source = requireText(source, "source is required");
        recordedAt = recordedAt == null ? Instant.now() : recordedAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
