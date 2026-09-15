package com.stackwork360.attendanceservice.application;

import com.stackwork360.attendanceservice.domain.AttendanceEntryType;
import java.time.Instant;

public record CorrectAttendanceCommand(
        Instant checkInAt,
        Instant checkOutAt,
        AttendanceEntryType type,
        String location,
        String reason
) {
}
