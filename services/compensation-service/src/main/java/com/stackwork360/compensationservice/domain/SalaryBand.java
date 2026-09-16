package com.stackwork360.compensationservice.domain;

import java.util.Objects;
import java.util.UUID;

public record SalaryBand(
        UUID id,
        String tenantId,
        String jobLevel,
        String location,
        Money minimum,
        Money midpoint,
        Money maximum
) {
    public SalaryBand {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        jobLevel = requireText(jobLevel, "job level is required");
        location = requireText(location, "location is required");
        Objects.requireNonNull(minimum, "minimum is required");
        Objects.requireNonNull(midpoint, "midpoint is required");
        Objects.requireNonNull(maximum, "maximum is required");
        if (minimum.compareTo(midpoint) > 0 || midpoint.compareTo(maximum) > 0) {
            throw new IllegalArgumentException("salary band must be minimum <= midpoint <= maximum");
        }
    }

    public boolean contains(Money salary) {
        return salary.between(minimum, maximum);
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
