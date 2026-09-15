package com.stackwork360.attendanceservice.domain;

import java.time.Duration;
import java.util.List;

public record AttendanceSummary(
        Duration workedTime,
        Duration overtime,
        List<AttendanceAnomaly> anomalies
) {
}
