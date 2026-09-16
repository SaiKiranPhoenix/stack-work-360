package com.stackwork360.compensationservice.api;

import com.stackwork360.compensationservice.application.RecordCompensationCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record RecordCompensationRequest(
        @NotBlank String workerId,
        @NotBlank String jobLevel,
        @NotBlank String location,
        @Valid @NotNull MoneyRequest salary,
        @NotNull LocalDate effectiveDate,
        @NotBlank String source
) {
    RecordCompensationCommand toCommand(String tenantId) {
        return new RecordCompensationCommand(tenantId, workerId, jobLevel, location, salary.toDomain(), effectiveDate, source);
    }
}
