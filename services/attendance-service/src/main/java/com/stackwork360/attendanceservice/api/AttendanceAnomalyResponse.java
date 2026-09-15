package com.stackwork360.attendanceservice.api;

import com.stackwork360.attendanceservice.domain.AttendanceAnomaly;
import com.stackwork360.attendanceservice.domain.AttendanceAnomalyType;

public record AttendanceAnomalyResponse(
        AttendanceAnomalyType type,
        String message
) {
    static AttendanceAnomalyResponse from(AttendanceAnomaly anomaly) {
        return new AttendanceAnomalyResponse(anomaly.type(), anomaly.message());
    }
}
