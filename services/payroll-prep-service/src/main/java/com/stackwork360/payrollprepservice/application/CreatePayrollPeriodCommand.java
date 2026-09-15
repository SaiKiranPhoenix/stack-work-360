package com.stackwork360.payrollprepservice.application;

import java.time.LocalDate;

public record CreatePayrollPeriodCommand(
        String tenantId,
        LocalDate startDate,
        LocalDate endDate
) {
}
