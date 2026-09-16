package com.stackwork360.benefitsservice.domain;

import java.util.Objects;
import java.util.UUID;

public record EligibilityRule(
        UUID id,
        String tenantId,
        String planCode,
        String employmentType,
        String region,
        int minimumTenureDays
) {
    public EligibilityRule {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        planCode = requireText(planCode, "plan code is required");
        employmentType = requireText(employmentType, "employment type is required");
        region = region == null || region.isBlank() ? "ANY" : region.trim();
        if (minimumTenureDays < 0) {
            throw new IllegalArgumentException("minimum tenure days cannot be negative");
        }
    }

    public boolean eligible(EmployeeProfile profile, java.time.LocalDate asOf) {
        boolean employmentMatches = employmentType.equalsIgnoreCase(profile.employmentType()) || employmentType.equalsIgnoreCase("ANY");
        boolean regionMatches = region.equalsIgnoreCase(profile.region()) || region.equalsIgnoreCase("ANY");
        return employmentMatches && regionMatches && profile.tenureDays(asOf) >= minimumTenureDays;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
