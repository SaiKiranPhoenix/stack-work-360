package com.stackwork360.compensationservice.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record CompensationBenchmark(
        UUID id,
        String tenantId,
        String jobLevel,
        String location,
        Money marketMedian,
        String source,
        Instant importedAt
) {
    public CompensationBenchmark {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        jobLevel = requireText(jobLevel, "job level is required");
        location = requireText(location, "location is required");
        Objects.requireNonNull(marketMedian, "market median is required");
        source = requireText(source, "source is required");
        importedAt = importedAt == null ? Instant.now() : importedAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
