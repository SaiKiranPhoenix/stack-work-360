package com.stackwork360.payrollprepservice.application;

import com.stackwork360.payrollprepservice.domain.Money;

public record AddPayrollInputCommand(
        String workerId,
        Money basePay
) {
}
