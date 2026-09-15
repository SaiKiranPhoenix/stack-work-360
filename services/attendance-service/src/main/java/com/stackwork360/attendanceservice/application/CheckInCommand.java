package com.stackwork360.attendanceservice.application;

import com.stackwork360.attendanceservice.domain.AttendanceEntryType;
import java.time.Instant;

public record CheckInCommand(
        String tenantId,
        String workerId,
        AttendanceEntryType type,
        Instant checkInAt,
        String location
) {
}
