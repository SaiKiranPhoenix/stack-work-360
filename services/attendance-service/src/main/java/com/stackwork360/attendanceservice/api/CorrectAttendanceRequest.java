package com.stackwork360.attendanceservice.api;

import com.stackwork360.attendanceservice.application.CorrectAttendanceCommand;
import com.stackwork360.attendanceservice.domain.AttendanceEntryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record CorrectAttendanceRequest(
        @NotNull Instant checkInAt,
        @NotNull Instant checkOutAt,
        @NotNull AttendanceEntryType type,
        String location,
        @NotBlank String reason
) {
    CorrectAttendanceCommand toCommand() {
        return new CorrectAttendanceCommand(checkInAt, checkOutAt, type, location, reason);
    }
}
