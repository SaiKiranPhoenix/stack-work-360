package com.stackwork360.payrollprepservice.application;

import com.stackwork360.payrollprepservice.domain.Money;

public record PayrollExportLine(
        String workerId,
        Money basePay,
        Money totalAdjustments,
        Money grossPay
) {
}
