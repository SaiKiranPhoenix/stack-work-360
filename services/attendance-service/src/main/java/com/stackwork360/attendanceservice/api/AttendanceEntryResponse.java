package com.stackwork360.attendanceservice.api;

import com.stackwork360.attendanceservice.domain.AttendanceEntry;
import com.stackwork360.attendanceservice.domain.AttendanceEntryStatus;
import com.stackwork360.attendanceservice.domain.AttendanceEntryType;
import java.time.Instant;
import java.util.UUID;

public record AttendanceEntryResponse(
        UUID id,
        String tenantId,
        String workerId,
        AttendanceEntryType type,
        Instant checkInAt,
        Instant checkOutAt,
        AttendanceEntryStatus status,
        String location,
        String correctionReason,
        long overtimeMinutes,
        Instant createdAt,
        Instant updatedAt
) {
    static AttendanceEntryResponse from(AttendanceEntry entry) {
        return new AttendanceEntryResponse(
                entry.id(),
                entry.tenantId(),
                entry.workerId(),
                entry.type(),
                entry.checkInAt(),
                entry.checkOutAt(),
                entry.status(),
                entry.location(),
                entry.correctionReason(),
                entry.overtime().toMinutes(),
                entry.createdAt(),
                entry.updatedAt()
        );
    }
}
