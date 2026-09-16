package com.stackwork360.benefitsservice.api;

import com.stackwork360.benefitsservice.application.EnrollEmployeeCommand;
import com.stackwork360.benefitsservice.domain.CoverageTier;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record EnrollEmployeeRequest(
        @Valid @NotNull EmployeeProfileRequest profile,
        @NotBlank String planCode,
        @NotNull CoverageTier coverageTier,
        @NotNull LocalDate coverageStart
) {
    EnrollEmployeeCommand toCommand(String tenantId) {
        return new EnrollEmployeeCommand(tenantId, profile.toDomain(), planCode, coverageTier, coverageStart);
    }
}
