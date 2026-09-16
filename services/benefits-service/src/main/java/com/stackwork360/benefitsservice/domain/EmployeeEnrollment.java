package com.stackwork360.benefitsservice.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record EmployeeEnrollment(
        UUID id,
        String tenantId,
        String workerId,
        String planCode,
        CoverageTier coverageTier,
        EnrollmentStatus status,
        LocalDate coverageStart,
        LocalDate coverageEnd,
        Instant updatedAt
) {
    public EmployeeEnrollment {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        workerId = requireText(workerId, "worker id is required");
        planCode = requireText(planCode, "plan code is required");
        coverageTier = Objects.requireNonNull(coverageTier, "coverage tier is required");
        status = Objects.requireNonNull(status, "status is required");
        coverageStart = Objects.requireNonNull(coverageStart, "coverage start date is required");
        if (coverageEnd != null && coverageEnd.isBefore(coverageStart)) {
            throw new IllegalArgumentException("coverage end date cannot be before start date");
        }
        updatedAt = updatedAt == null ? Instant.now() : updatedAt;
    }

    public static EmployeeEnrollment active(String tenantId, String workerId, String planCode, CoverageTier tier, LocalDate coverageStart) {
        return new EmployeeEnrollment(null, tenantId, workerId, planCode, tier, tier == CoverageTier.WAIVED ? EnrollmentStatus.WAIVED : EnrollmentStatus.ACTIVE, coverageStart, null, null);
    }

    public EmployeeEnrollment withTier(CoverageTier tier, LocalDate effectiveDate) {
        return active(tenantId, workerId, planCode, tier, effectiveDate);
    }

    public EmployeeEnrollment cancelled(LocalDate effectiveDate) {
        return new EmployeeEnrollment(id, tenantId, workerId, planCode, coverageTier, EnrollmentStatus.CANCELLED, coverageStart, effectiveDate, null);
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
