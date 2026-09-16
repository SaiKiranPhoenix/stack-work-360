package com.stackwork360.benefitsservice.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;

public record BenefitPlan(
        UUID id,
        String tenantId,
        String planCode,
        String name,
        BenefitPlanType type,
        BigDecimal monthlyEmployerCost,
        boolean active
) {
    public BenefitPlan {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        planCode = requireText(planCode, "plan code is required");
        name = requireText(name, "plan name is required");
        type = Objects.requireNonNull(type, "plan type is required");
        monthlyEmployerCost = Objects.requireNonNull(monthlyEmployerCost, "monthly employer cost is required").setScale(2, RoundingMode.HALF_UP);
        if (monthlyEmployerCost.signum() < 0) {
            throw new IllegalArgumentException("monthly employer cost cannot be negative");
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
