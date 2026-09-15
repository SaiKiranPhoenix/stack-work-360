package com.stackwork360.payrollprepservice.api;

import com.stackwork360.payrollprepservice.application.PayrollExport;
import com.stackwork360.payrollprepservice.domain.PayrollPeriodStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record PayrollExportResponse(
        UUID periodId,
        String tenantId,
        LocalDate startDate,
        LocalDate endDate,
        PayrollPeriodStatus status,
        MoneyResponse totalGrossPay,
        List<PayrollExportLineResponse> lines
) {
    static PayrollExportResponse from(PayrollExport export) {
        return new PayrollExportResponse(
                export.periodId(),
                export.tenantId(),
                export.startDate(),
                export.endDate(),
                export.status(),
                MoneyResponse.from(export.totalGrossPay()),
                export.lines().stream().map(PayrollExportLineResponse::from).toList()
        );
    }
}
