package com.stackwork360.compensationservice.api;

import com.stackwork360.compensationservice.domain.CompensationHistory;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record CompensationHistoryResponse(
        UUID id,
        String tenantId,
        String workerId,
        String jobLevel,
        String location,
        MoneyResponse salary,
        LocalDate effectiveDate,
        String source,
        Instant recordedAt
) {
    static CompensationHistoryResponse from(CompensationHistory history) {
        return new CompensationHistoryResponse(
                history.id(),
                history.tenantId(),
                history.workerId(),
                history.jobLevel(),
                history.location(),
                MoneyResponse.from(history.salary()),
                history.effectiveDate(),
                history.source(),
                history.recordedAt()
        );
    }
}
