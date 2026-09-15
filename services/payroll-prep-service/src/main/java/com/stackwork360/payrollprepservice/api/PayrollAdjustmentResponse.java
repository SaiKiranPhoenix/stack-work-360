package com.stackwork360.payrollprepservice.api;

import com.stackwork360.payrollprepservice.domain.PayrollAdjustment;
import com.stackwork360.payrollprepservice.domain.PayrollAdjustmentType;
import java.util.UUID;

public record PayrollAdjustmentResponse(
        UUID id,
        PayrollAdjustmentType type,
        MoneyResponse amount,
        MoneyResponse signedAmount,
        String reason
) {
    static PayrollAdjustmentResponse from(PayrollAdjustment adjustment) {
        return new PayrollAdjustmentResponse(
                adjustment.id(),
                adjustment.type(),
                MoneyResponse.from(adjustment.amount()),
                MoneyResponse.from(adjustment.signedAmount()),
                adjustment.reason()
        );
    }
}
