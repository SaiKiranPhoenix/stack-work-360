package com.stackwork360.attendanceservice.api;

import com.stackwork360.attendanceservice.application.RemoteWorkCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record RemoteWorkRequest(
        @NotBlank String workerId,
        @NotNull Instant startAt,
        @NotNull Instant endAt,
        String location
) {
    RemoteWorkCommand toCommand(String tenantId) {
        return new RemoteWorkCommand(tenantId, workerId, startAt, endAt, location);
    }
}
