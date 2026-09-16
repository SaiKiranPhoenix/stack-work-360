package com.stackwork360.benefitsservice.api;

import com.stackwork360.benefitsservice.domain.CoverageTier;
import com.stackwork360.benefitsservice.domain.EmployeeEnrollment;
import com.stackwork360.benefitsservice.domain.EnrollmentStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeEnrollmentResponse(
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
    static EmployeeEnrollmentResponse from(EmployeeEnrollment enrollment) {
        return new EmployeeEnrollmentResponse(
                enrollment.id(),
                enrollment.tenantId(),
                enrollment.workerId(),
                enrollment.planCode(),
                enrollment.coverageTier(),
                enrollment.status(),
                enrollment.coverageStart(),
                enrollment.coverageEnd(),
                enrollment.updatedAt()
        );
    }
}
