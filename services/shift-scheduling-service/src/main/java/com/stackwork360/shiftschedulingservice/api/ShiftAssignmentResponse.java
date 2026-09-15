package com.stackwork360.shiftschedulingservice.api;

import com.stackwork360.shiftschedulingservice.domain.ShiftAssignment;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ShiftAssignmentResponse(
        UUID id,
        String tenantId,
        UUID templateId,
        String workerId,
        LocalDate shiftDate,
        Instant startsAt,
        Instant endsAt,
        Instant createdAt
) {
    static ShiftAssignmentResponse from(ShiftAssignment assignment) {
        return new ShiftAssignmentResponse(
                assignment.id(),
                assignment.tenantId(),
                assignment.templateId(),
                assignment.workerId(),
                assignment.shiftDate(),
                assignment.startsAt(),
                assignment.endsAt(),
                assignment.createdAt()
        );
    }
}
