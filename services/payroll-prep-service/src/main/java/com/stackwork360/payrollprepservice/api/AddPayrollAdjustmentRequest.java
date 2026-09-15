package com.stackwork360.payrollprepservice.api;

import com.stackwork360.payrollprepservice.domain.PayrollAdjustmentType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddPayrollAdjustmentRequest(
        @NotBlank String workerId,
        @NotNull PayrollAdjustmentType type,
        @Valid @NotNull MoneyRequest amount,
        @NotBlank String reason
) {
}
