package com.stackwork360.benefitsservice.api;

import com.stackwork360.benefitsservice.domain.EligibilityRule;
import java.util.UUID;

public record EligibilityRuleResponse(
        UUID id,
        String tenantId,
        String planCode,
        String employmentType,
        String region,
        int minimumTenureDays
) {
    static EligibilityRuleResponse from(EligibilityRule rule) {
        return new EligibilityRuleResponse(rule.id(), rule.tenantId(), rule.planCode(), rule.employmentType(), rule.region(), rule.minimumTenureDays());
    }
}
