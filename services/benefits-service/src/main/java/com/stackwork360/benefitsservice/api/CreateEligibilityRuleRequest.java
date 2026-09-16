package com.stackwork360.benefitsservice.api;

import com.stackwork360.benefitsservice.application.CreateEligibilityRuleCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateEligibilityRuleRequest(
        @NotBlank String planCode,
        @NotBlank String employmentType,
        String region,
        @Min(0) int minimumTenureDays
) {
    CreateEligibilityRuleCommand toCommand(String tenantId) {
        return new CreateEligibilityRuleCommand(tenantId, planCode, employmentType, region, minimumTenureDays);
    }
}
