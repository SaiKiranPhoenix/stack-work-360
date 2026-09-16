package com.stackwork360.benefitsservice.application;

import com.stackwork360.benefitsservice.domain.BenefitPlanType;
import java.math.BigDecimal;

public record CreateBenefitPlanCommand(
        String tenantId,
        String planCode,
        String name,
        BenefitPlanType type,
        BigDecimal monthlyEmployerCost,
        boolean active
) {
}
