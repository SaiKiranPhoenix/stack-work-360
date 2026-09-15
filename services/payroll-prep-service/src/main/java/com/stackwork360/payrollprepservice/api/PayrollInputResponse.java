package com.stackwork360.payrollprepservice.api;

import com.stackwork360.payrollprepservice.domain.PayrollInput;
import java.util.List;
import java.util.UUID;

public record PayrollInputResponse(
        UUID id,
        String workerId,
        MoneyResponse basePay,
        MoneyResponse grossPay,
        List<PayrollAdjustmentResponse> adjustments
) {
    static PayrollInputResponse from(PayrollInput input) {
        return new PayrollInputResponse(
                input.id(),
                input.workerId(),
                MoneyResponse.from(input.basePay()),
                MoneyResponse.from(input.grossPay()),
                input.adjustments().stream().map(PayrollAdjustmentResponse::from).toList()
        );
    }
}
