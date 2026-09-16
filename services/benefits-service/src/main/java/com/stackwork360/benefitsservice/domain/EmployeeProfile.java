package com.stackwork360.benefitsservice.domain;

import java.time.LocalDate;
import java.util.Objects;

public record EmployeeProfile(
        String workerId,
        String employmentType,
        String region,
        LocalDate hireDate
) {
    public EmployeeProfile {
        workerId = requireText(workerId, "worker id is required");
        employmentType = requireText(employmentType, "employment type is required");
        region = requireText(region, "region is required");
        hireDate = Objects.requireNonNull(hireDate, "hire date is required");
    }

    public long tenureDays(LocalDate asOf) {
        return java.time.temporal.ChronoUnit.DAYS.between(hireDate, asOf);
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
