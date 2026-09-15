package com.stackwork360.attendanceservice.application;

import java.time.Instant;

public record RemoteWorkCommand(
        String tenantId,
        String workerId,
        Instant startAt,
        Instant endAt,
        String location
) {
}
