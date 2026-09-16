package com.stackwork360.workforceplanningservice.application;

import com.stackwork360.workforceplanningservice.domain.EmploymentType;
import java.time.LocalDate;

public record CreateHiringPlanCommand(
        String tenantId,
        String teamId,
        String roleName,
        String location,
        int openings,
        LocalDate targetStartDate,
        EmploymentType employmentType
) {
}
