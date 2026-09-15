package com.stackwork360.attendanceservice.api;

import com.stackwork360.attendanceservice.application.CheckOutCommand;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record CheckOutRequest(
        @NotNull Instant checkOutAt
) {
    CheckOutCommand toCommand() {
        return new CheckOutCommand(checkOutAt);
    }
}
