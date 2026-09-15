package com.stackwork360.shiftschedulingservice.api;

import com.stackwork360.shiftschedulingservice.application.CreateRotatingScheduleCommand;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateRotatingScheduleRequest(
        @NotNull UUID templateId,
        @NotEmpty List<String> workerRotation,
        @NotNull LocalDate startsOn,
        @NotNull LocalDate throughDate
) {
    CreateRotatingScheduleCommand toCommand(String tenantId) {
        return new CreateRotatingScheduleCommand(tenantId, templateId, workerRotation, startsOn);
    }
}
