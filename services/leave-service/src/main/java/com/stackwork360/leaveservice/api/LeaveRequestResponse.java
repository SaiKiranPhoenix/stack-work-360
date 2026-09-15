package com.stackwork360.leaveservice.api;

import com.stackwork360.leaveservice.domain.LeaveRequest;
import com.stackwork360.leaveservice.domain.LeaveRequestStatus;
import com.stackwork360.leaveservice.domain.LeaveType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record LeaveRequestResponse(
        UUID id,
        String workerId,
        LeaveType leaveType,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal requestedDays,
        String reason,
        LeaveRequestStatus status,
        String decidedBy,
        Instant decidedAt,
        Instant createdAt,
        Instant updatedAt
) {
    public static LeaveRequestResponse from(LeaveRequest request) {
        return new LeaveRequestResponse(
                request.id(),
                request.workerId(),
                request.leaveType(),
                request.startDate(),
                request.endDate(),
                request.requestedDays(),
                request.reason(),
                request.status(),
                request.decidedBy(),
                request.decidedAt(),
                request.createdAt(),
                request.updatedAt()
        );
    }
}
