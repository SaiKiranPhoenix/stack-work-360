package com.stackwork360.attendanceservice.api;

import com.stackwork360.attendanceservice.application.CheckInCommand;
import com.stackwork360.attendanceservice.domain.AttendanceEntryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record CheckInRequest(
        @NotBlank String workerId,
        @NotNull AttendanceEntryType type,
        @NotNull Instant checkInAt,
        String location
) {
    CheckInCommand toCommand(String tenantId) {
        return new CheckInCommand(tenantId, workerId, type, checkInAt, location);
    }
}
