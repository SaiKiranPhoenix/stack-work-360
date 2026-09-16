package com.stackwork360.benefitsservice.application;

public record CreateEligibilityRuleCommand(
        String tenantId,
        String planCode,
        String employmentType,
        String region,
        int minimumTenureDays
) {
}
