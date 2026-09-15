package com.stackwork360.attendanceservice.application;

import java.time.Instant;

public record CheckOutCommand(
        Instant checkOutAt
) {
}
