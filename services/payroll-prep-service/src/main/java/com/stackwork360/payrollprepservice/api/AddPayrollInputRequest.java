package com.stackwork360.payrollprepservice.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddPayrollInputRequest(
        @NotBlank String workerId,
        @Valid @NotNull MoneyRequest basePay
) {
}
