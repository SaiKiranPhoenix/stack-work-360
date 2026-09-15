package com.stackwork360.payrollprepservice.application;

import com.stackwork360.payrollprepservice.domain.Money;
import com.stackwork360.payrollprepservice.domain.PayrollAdjustmentType;

public record AddPayrollAdjustmentCommand(
        String workerId,
        PayrollAdjustmentType type,
        Money amount,
        String reason
) {
}
