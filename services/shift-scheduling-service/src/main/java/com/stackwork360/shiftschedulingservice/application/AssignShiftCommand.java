package com.stackwork360.shiftschedulingservice.application;

import java.time.LocalDate;
import java.util.UUID;

public record AssignShiftCommand(
        UUID templateId,
        String workerId,
        LocalDate shiftDate
) {
}
