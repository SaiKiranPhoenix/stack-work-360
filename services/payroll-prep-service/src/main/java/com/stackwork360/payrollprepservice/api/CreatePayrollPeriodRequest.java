package com.stackwork360.payrollprepservice.api;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreatePayrollPeriodRequest(
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {
}
