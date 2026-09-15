package com.stackwork360.riskengineservice.application;

import com.stackwork360.riskengineservice.domain.RiskOperator;

public record RiskConditionCommand(
        String factKey,
        RiskOperator operator,
        String expectedValue
) {
}
