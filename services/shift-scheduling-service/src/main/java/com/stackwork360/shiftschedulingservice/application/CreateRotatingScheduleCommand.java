package com.stackwork360.shiftschedulingservice.application;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateRotatingScheduleCommand(
        String tenantId,
        UUID templateId,
        List<String> workerRotation,
        LocalDate startsOn
) {
}
