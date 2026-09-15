package com.stackwork360.payrollprepservice.domain;

import java.util.Objects;
import java.util.UUID;

public record PayrollAdjustment(
        UUID id,
        PayrollAdjustmentType type,
        Money amount,
        String reason
) {
    public PayrollAdjustment {
        id = id == null ? UUID.randomUUID() : id;
        type = Objects.requireNonNull(type, "adjustment type is required");
        amount = Objects.requireNonNull(amount, "adjustment amount is required");
        reason = requireText(reason, "adjustment reason is required");
    }

    public Money signedAmount() {
        return switch (type) {
            case BONUS, REIMBURSEMENT, OVERTIME, CONTRACTOR_INVOICE -> amount;
            case DEDUCTION, UNPAID_LEAVE -> amount.negate();
        };
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
