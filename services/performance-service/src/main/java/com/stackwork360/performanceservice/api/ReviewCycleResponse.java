package com.stackwork360.performanceservice.api;

import com.stackwork360.performanceservice.domain.ReviewCycle;
import com.stackwork360.performanceservice.domain.ReviewCycleStatus;
import java.time.LocalDate;
import java.util.UUID;

public record ReviewCycleResponse(
        UUID id,
        String tenantId,
        String name,
        LocalDate startsOn,
        LocalDate endsOn,
        ReviewCycleStatus status
) {
    static ReviewCycleResponse from(ReviewCycle cycle) {
        return new ReviewCycleResponse(cycle.id(), cycle.tenantId(), cycle.name(), cycle.startsOn(), cycle.endsOn(), cycle.status());
    }
}
