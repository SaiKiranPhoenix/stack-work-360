package com.stackwork360.payrollprepservice.api;

import com.stackwork360.payrollprepservice.application.PayrollExportLine;

public record PayrollExportLineResponse(
        String workerId,
        MoneyResponse basePay,
        MoneyResponse totalAdjustments,
        MoneyResponse grossPay
) {
    static PayrollExportLineResponse from(PayrollExportLine line) {
        return new PayrollExportLineResponse(
                line.workerId(),
                MoneyResponse.from(line.basePay()),
                MoneyResponse.from(line.totalAdjustments()),
                MoneyResponse.from(line.grossPay())
        );
    }
}
