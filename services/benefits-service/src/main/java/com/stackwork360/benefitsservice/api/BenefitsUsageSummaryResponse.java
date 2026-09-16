package com.stackwork360.benefitsservice.api;

import com.stackwork360.benefitsservice.domain.BenefitsUsageSummary;
import java.math.BigDecimal;

public record BenefitsUsageSummaryResponse(
        String tenantId,
        int activeEnrollments,
        int waivedEnrollments,
        BigDecimal monthlyEmployerCost
) {
    static BenefitsUsageSummaryResponse from(BenefitsUsageSummary summary) {
        return new BenefitsUsageSummaryResponse(summary.tenantId(), summary.activeEnrollments(), summary.waivedEnrollments(), summary.monthlyEmployerCost());
    }
}
