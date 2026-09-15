package com.stackwork360.payrollprepservice.application;

import com.stackwork360.payrollprepservice.domain.Money;
import java.util.UUID;

public record PayrollVarianceReport(
        UUID periodId,
        int workerCount,
        long adjustmentCount,
        Money totalAdjustments,
        Money totalGrossPay
) {
}
