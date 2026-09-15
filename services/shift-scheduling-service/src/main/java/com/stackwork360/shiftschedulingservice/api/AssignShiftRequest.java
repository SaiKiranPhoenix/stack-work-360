package com.stackwork360.shiftschedulingservice.api;

import com.stackwork360.shiftschedulingservice.application.AssignShiftCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record AssignShiftRequest(
        @NotNull UUID templateId,
        @NotBlank String workerId,
        @NotNull LocalDate shiftDate
) {
    AssignShiftCommand toCommand() {
        return new AssignShiftCommand(templateId, workerId, shiftDate);
    }
}
