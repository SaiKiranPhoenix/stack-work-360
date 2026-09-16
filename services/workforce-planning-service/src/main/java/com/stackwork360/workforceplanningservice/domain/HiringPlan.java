package com.stackwork360.workforceplanningservice.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record HiringPlan(
        UUID id,
        String tenantId,
        String teamId,
        String roleName,
        String location,
        int openings,
        LocalDate targetStartDate,
        EmploymentType employmentType
) {
    public HiringPlan {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        teamId = requireText(teamId, "team id is required");
        roleName = requireText(roleName, "role name is required");
        location = requireText(location, "location is required");
        if (openings <= 0) {
            throw new IllegalArgumentException("openings must be positive");
        }
        targetStartDate = Objects.requireNonNull(targetStartDate, "target start date is required");
        employmentType = Objects.requireNonNull(employmentType, "employment type is required");
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
