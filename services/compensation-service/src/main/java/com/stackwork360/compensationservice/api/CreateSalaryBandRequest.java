package com.stackwork360.compensationservice.api;

import com.stackwork360.compensationservice.application.CreateSalaryBandCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSalaryBandRequest(
        @NotBlank String jobLevel,
        @NotBlank String location,
        @Valid @NotNull MoneyRequest minimum,
        @Valid @NotNull MoneyRequest midpoint,
        @Valid @NotNull MoneyRequest maximum
) {
    CreateSalaryBandCommand toCommand(String tenantId) {
        return new CreateSalaryBandCommand(tenantId, jobLevel, location, minimum.toDomain(), midpoint.toDomain(), maximum.toDomain());
    }
}
