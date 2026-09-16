package com.stackwork360.benefitsservice.domain;

import java.math.BigDecimal;

public record BenefitsUsageSummary(
        String tenantId,
        int activeEnrollments,
        int waivedEnrollments,
        BigDecimal monthlyEmployerCost
) {
}
