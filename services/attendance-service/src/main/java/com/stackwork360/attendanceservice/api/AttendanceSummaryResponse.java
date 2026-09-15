package com.stackwork360.attendanceservice.api;

import com.stackwork360.attendanceservice.domain.AttendanceSummary;
import java.util.List;

public record AttendanceSummaryResponse(
        long workedMinutes,
        long overtimeMinutes,
        List<AttendanceAnomalyResponse> anomalies
) {
    static AttendanceSummaryResponse from(AttendanceSummary summary) {
        return new AttendanceSummaryResponse(
                summary.workedTime().toMinutes(),
                summary.overtime().toMinutes(),
                summary.anomalies().stream().map(AttendanceAnomalyResponse::from).toList()
        );
    }
}
