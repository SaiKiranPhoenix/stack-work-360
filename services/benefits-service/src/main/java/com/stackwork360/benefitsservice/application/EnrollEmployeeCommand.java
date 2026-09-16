package com.stackwork360.benefitsservice.application;

import com.stackwork360.benefitsservice.domain.CoverageTier;
import com.stackwork360.benefitsservice.domain.EmployeeProfile;
import java.time.LocalDate;

public record EnrollEmployeeCommand(
        String tenantId,
        EmployeeProfile profile,
        String planCode,
        CoverageTier coverageTier,
        LocalDate coverageStart
) {
}
