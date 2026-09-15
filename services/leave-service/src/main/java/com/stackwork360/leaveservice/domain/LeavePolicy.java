package com.stackwork360.leaveservice.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record LeavePolicy(
        String tenantId,
        LeaveType leaveType,
        BigDecimal annualAllowanceDays,
        boolean requiresApproval,
        boolean paid
) {
    public LeavePolicy {
        tenantId = requireText(tenantId, "tenant id is required");
        leaveType = Objects.requireNonNull(leaveType, "leave type is required");
        Objects.requireNonNull(annualAllowanceDays, "annual allowance is required");
        if (annualAllowanceDays.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("annual allowance cannot be negative");
        }
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
