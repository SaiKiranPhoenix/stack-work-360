package com.stackwork360.peoplecoreservice.application;

import com.stackwork360.peoplecoreservice.domain.EmploymentType;
import java.time.LocalDate;

public record CreateWorkerCommand(
        String tenantId,
        String employeeNumber,
        String firstName,
        String lastName,
        String workEmail,
        EmploymentType employmentType,
        String jobTitle,
        String departmentId,
        String managerWorkerId,
        LocalDate startDate
) {
}
