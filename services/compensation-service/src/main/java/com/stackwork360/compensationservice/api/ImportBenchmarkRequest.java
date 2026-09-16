package com.stackwork360.compensationservice.api;

import com.stackwork360.compensationservice.application.ImportBenchmarkCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ImportBenchmarkRequest(
        @NotBlank String jobLevel,
        @NotBlank String location,
        @Valid @NotNull MoneyRequest marketMedian,
        @NotBlank String source
) {
    ImportBenchmarkCommand toCommand(String tenantId) {
        return new ImportBenchmarkCommand(tenantId, jobLevel, location, marketMedian.toDomain(), source);
    }
}
