package com.stackwork360.riskengineservice.api;

import com.stackwork360.riskengineservice.application.RiskConditionCommand;
import com.stackwork360.riskengineservice.domain.RiskOperator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RiskConditionRequest(
        @NotBlank String factKey,
        @NotNull RiskOperator operator,
        @NotBlank String expectedValue
) {
    RiskConditionCommand toCommand() {
        return new RiskConditionCommand(factKey, operator, expectedValue);
    }
}
