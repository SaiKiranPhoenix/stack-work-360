package com.stackwork360.compensationservice.api;

import com.stackwork360.compensationservice.application.RequestCompensationChangeCommand;
import com.stackwork360.compensationservice.domain.CompensationChangeReason;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record RequestCompensationChangeRequest(
        @NotBlank String workerId,
        @NotBlank String currentJobLevel,
        @NotBlank String proposedJobLevel,
        @Valid @NotNull MoneyRequest currentSalary,
        @Valid @NotNull MoneyRequest proposedSalary,
        @NotNull LocalDate effectiveDate,
        @NotNull CompensationChangeReason reason
) {
    RequestCompensationChangeCommand toCommand(String tenantId) {
        return new RequestCompensationChangeCommand(
                tenantId,
                workerId,
                currentJobLevel,
                proposedJobLevel,
                currentSalary.toDomain(),
                proposedSalary.toDomain(),
                effectiveDate,
                reason
        );
    }
}
