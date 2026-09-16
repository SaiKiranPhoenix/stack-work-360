package com.stackwork360.workforceplanningservice.api;

import com.stackwork360.workforceplanningservice.application.CreateHiringPlanCommand;
import com.stackwork360.workforceplanningservice.domain.EmploymentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateHiringPlanRequest(
        @NotBlank String teamId,
        @NotBlank String roleName,
        @NotBlank String location,
        @Min(1) int openings,
        @NotNull LocalDate targetStartDate,
        @NotNull EmploymentType employmentType
) {
    CreateHiringPlanCommand toCommand(String tenantId) {
        return new CreateHiringPlanCommand(tenantId, teamId, roleName, location, openings, targetStartDate, employmentType);
    }
}
