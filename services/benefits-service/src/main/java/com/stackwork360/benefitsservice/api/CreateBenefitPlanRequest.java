package com.stackwork360.benefitsservice.api;

import com.stackwork360.benefitsservice.application.CreateBenefitPlanCommand;
import com.stackwork360.benefitsservice.domain.BenefitPlanType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreateBenefitPlanRequest(
        @NotBlank String planCode,
        @NotBlank String name,
        @NotNull BenefitPlanType type,
        @NotNull BigDecimal monthlyEmployerCost,
        boolean active
) {
    CreateBenefitPlanCommand toCommand(String tenantId) {
        return new CreateBenefitPlanCommand(tenantId, planCode, name, type, monthlyEmployerCost, active);
    }
}
