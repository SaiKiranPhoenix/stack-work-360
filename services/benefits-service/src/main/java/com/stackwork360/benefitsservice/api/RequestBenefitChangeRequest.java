package com.stackwork360.benefitsservice.api;

import com.stackwork360.benefitsservice.application.RequestBenefitChangeCommand;
import com.stackwork360.benefitsservice.domain.BenefitChangeType;
import com.stackwork360.benefitsservice.domain.CoverageTier;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record RequestBenefitChangeRequest(
        @Valid @NotNull EmployeeProfileRequest profile,
        @NotBlank String planCode,
        @NotNull BenefitChangeType type,
        @NotNull CoverageTier requestedTier,
        @NotNull LocalDate effectiveDate,
        @NotNull LocalDate requestedOn
) {
    RequestBenefitChangeCommand toCommand(String tenantId) {
        return new RequestBenefitChangeCommand(tenantId, profile.toDomain(), planCode, type, requestedTier, effectiveDate, requestedOn);
    }
}
