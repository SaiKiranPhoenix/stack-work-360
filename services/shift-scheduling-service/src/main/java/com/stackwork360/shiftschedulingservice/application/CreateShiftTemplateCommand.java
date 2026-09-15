package com.stackwork360.shiftschedulingservice.application;

import java.time.LocalTime;

public record CreateShiftTemplateCommand(
        String tenantId,
        String name,
        LocalTime startTime,
        LocalTime endTime,
        int requiredStaff
) {
}
