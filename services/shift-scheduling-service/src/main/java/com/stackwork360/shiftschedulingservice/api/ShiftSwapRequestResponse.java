package com.stackwork360.shiftschedulingservice.api;

import com.stackwork360.shiftschedulingservice.domain.ShiftSwapRequest;
import com.stackwork360.shiftschedulingservice.domain.ShiftSwapStatus;
import java.time.Instant;
import java.util.UUID;

public record ShiftSwapRequestResponse(
        UUID id,
        String tenantId,
        UUID assignmentId,
        String requestedBy,
        String targetWorkerId,
        String reason,
        ShiftSwapStatus status,
        String decidedBy,
        Instant createdAt,
        Instant updatedAt
) {
    static ShiftSwapRequestResponse from(ShiftSwapRequest request) {
        return new ShiftSwapRequestResponse(
                request.id(),
                request.tenantId(),
                request.assignmentId(),
                request.requestedBy(),
                request.targetWorkerId(),
                request.reason(),
                request.status(),
                request.decidedBy(),
                request.createdAt(),
                request.updatedAt()
        );
    }
}
