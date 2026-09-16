package com.stackwork360.compensationservice.api;

import com.stackwork360.compensationservice.domain.CompensationChangeReason;
import com.stackwork360.compensationservice.domain.CompensationChangeRequest;
import com.stackwork360.compensationservice.domain.CompensationChangeStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record CompensationChangeRequestResponse(
        UUID id,
        String tenantId,
        String workerId,
        String currentJobLevel,
        String proposedJobLevel,
        MoneyResponse currentSalary,
        MoneyResponse proposedSalary,
        MoneyResponse budgetImpact,
        LocalDate effectiveDate,
        CompensationChangeReason reason,
        boolean promotion,
        CompensationChangeStatus status,
        String decidedBy,
        Instant createdAt,
        Instant updatedAt
) {
    static CompensationChangeRequestResponse from(CompensationChangeRequest request) {
        return new CompensationChangeRequestResponse(
                request.id(),
                request.tenantId(),
                request.workerId(),
                request.currentJobLevel(),
                request.proposedJobLevel(),
                MoneyResponse.from(request.currentSalary()),
                MoneyResponse.from(request.proposedSalary()),
                MoneyResponse.from(request.budgetImpact()),
                request.effectiveDate(),
                request.reason(),
                request.promotion(),
                request.status(),
                request.decidedBy(),
                request.createdAt(),
                request.updatedAt()
        );
    }
}
