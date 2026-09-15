package com.stackwork360.attendanceservice.domain;

public record AttendanceAnomaly(
        AttendanceAnomalyType type,
        String message
) {
}
