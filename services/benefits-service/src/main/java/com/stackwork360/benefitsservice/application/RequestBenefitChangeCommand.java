package com.stackwork360.benefitsservice.application;

import com.stackwork360.benefitsservice.domain.BenefitChangeType;
import com.stackwork360.benefitsservice.domain.CoverageTier;
import com.stackwork360.benefitsservice.domain.EmployeeProfile;
import java.time.LocalDate;

public record RequestBenefitChangeCommand(
        String tenantId,
        EmployeeProfile profile,
        String planCode,
        BenefitChangeType type,
        CoverageTier requestedTier,
        LocalDate effectiveDate,
        LocalDate requestedOn
) {
}
