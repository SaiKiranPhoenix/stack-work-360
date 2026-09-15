package com.stackwork360.peoplecoreservice.api;

import com.stackwork360.peoplecoreservice.domain.EmploymentType;
import com.stackwork360.peoplecoreservice.domain.Worker;
import com.stackwork360.peoplecoreservice.domain.WorkerStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record WorkerResponse(
        UUID id,
        String tenantId,
        String employeeNumber,
        String firstName,
        String lastName,
        String displayName,
        String workEmail,
        EmploymentType employmentType,
        WorkerStatus status,
        String jobTitle,
        String departmentId,
        String managerWorkerId,
        LocalDate startDate,
        LocalDate endDate,
        Instant createdAt,
        Instant updatedAt
) {
    public static WorkerResponse from(Worker worker) {
        return new WorkerResponse(
                worker.id(),
                worker.tenantId(),
                worker.employeeNumber(),
                worker.firstName(),
                worker.lastName(),
                worker.displayName(),
                worker.workEmail(),
                worker.employmentType(),
                worker.status(),
                worker.jobTitle(),
                worker.departmentId(),
                worker.managerWorkerId(),
                worker.startDate(),
                worker.endDate(),
                worker.createdAt(),
                worker.updatedAt()
        );
    }
}
