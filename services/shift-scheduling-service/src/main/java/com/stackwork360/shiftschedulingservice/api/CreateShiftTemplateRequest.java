package com.stackwork360.shiftschedulingservice.api;

import com.stackwork360.shiftschedulingservice.application.CreateShiftTemplateCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record CreateShiftTemplateRequest(
        @NotBlank String name,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime,
        @Min(1) int requiredStaff
) {
    CreateShiftTemplateCommand toCommand(String tenantId) {
        return new CreateShiftTemplateCommand(tenantId, name, startTime, endTime, requiredStaff);
    }
}
