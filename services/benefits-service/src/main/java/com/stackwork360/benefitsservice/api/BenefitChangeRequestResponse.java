package com.stackwork360.benefitsservice.api;

import com.stackwork360.benefitsservice.domain.BenefitChangeRequest;
import com.stackwork360.benefitsservice.domain.BenefitChangeStatus;
import com.stackwork360.benefitsservice.domain.BenefitChangeType;
import com.stackwork360.benefitsservice.domain.CoverageTier;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record BenefitChangeRequestResponse(
        UUID id,
        String tenantId,
        String workerId,
        String planCode,
        BenefitChangeType type,
        CoverageTier requestedTier,
        LocalDate effectiveDate,
        BenefitChangeStatus status,
        String decidedBy,
        Instant createdAt,
        Instant updatedAt
) {
    static BenefitChangeRequestResponse from(BenefitChangeRequest request) {
        return new BenefitChangeRequestResponse(
                request.id(),
                request.tenantId(),
                request.workerId(),
                request.planCode(),
                request.type(),
                request.requestedTier(),
                request.effectiveDate(),
                request.status(),
                request.decidedBy(),
                request.createdAt(),
                request.updatedAt()
        );
    }
}
