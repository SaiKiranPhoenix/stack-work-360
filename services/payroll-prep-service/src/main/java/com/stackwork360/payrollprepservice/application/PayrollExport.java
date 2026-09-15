package com.stackwork360.payrollprepservice.application;

import com.stackwork360.payrollprepservice.domain.Money;
import com.stackwork360.payrollprepservice.domain.PayrollPeriodStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PayrollExport(
        UUID periodId,
        String tenantId,
        LocalDate startDate,
        LocalDate endDate,
        PayrollPeriodStatus status,
        Money totalGrossPay,
        List<PayrollExportLine> lines
) {
}
