package com.stackwork360.benefitsservice.api;

import com.stackwork360.benefitsservice.domain.BenefitPlan;
import com.stackwork360.benefitsservice.domain.BenefitPlanType;
import java.math.BigDecimal;
import java.util.UUID;

public record BenefitPlanResponse(
        UUID id,
        String tenantId,
        String planCode,
        String name,
        BenefitPlanType type,
        BigDecimal monthlyEmployerCost,
        boolean active
) {
    static BenefitPlanResponse from(BenefitPlan plan) {
        return new BenefitPlanResponse(plan.id(), plan.tenantId(), plan.planCode(), plan.name(), plan.type(), plan.monthlyEmployerCost(), plan.active());
    }
}
