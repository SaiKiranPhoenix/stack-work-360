package com.stackwork360.workforceplanningservice.api;

import com.stackwork360.workforceplanningservice.domain.EmploymentType;
import com.stackwork360.workforceplanningservice.domain.HiringPlan;
import java.time.LocalDate;
import java.util.UUID;

public record HiringPlanResponse(UUID id, String tenantId, String teamId, String roleName, String location, int openings, LocalDate targetStartDate, EmploymentType employmentType) {
    static HiringPlanResponse from(HiringPlan plan) {
        return new HiringPlanResponse(plan.id(), plan.tenantId(), plan.teamId(), plan.roleName(), plan.location(), plan.openings(), plan.targetStartDate(), plan.employmentType());
    }
}
