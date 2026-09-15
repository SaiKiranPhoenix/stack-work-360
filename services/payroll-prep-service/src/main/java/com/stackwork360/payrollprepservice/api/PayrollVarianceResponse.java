package com.stackwork360.payrollprepservice.api;

import com.stackwork360.payrollprepservice.application.PayrollVarianceReport;
import java.util.UUID;

public record PayrollVarianceResponse(
        UUID periodId,
        int workerCount,
        long adjustmentCount,
        MoneyResponse totalAdjustments,
        MoneyResponse totalGrossPay
) {
    static PayrollVarianceResponse from(PayrollVarianceReport report) {
        return new PayrollVarianceResponse(
                report.periodId(),
                report.workerCount(),
                report.adjustmentCount(),
                MoneyResponse.from(report.totalAdjustments()),
                MoneyResponse.from(report.totalGrossPay())
        );
    }
}
