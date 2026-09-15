package com.stackwork360.riskengineservice.api;

import com.stackwork360.riskengineservice.domain.RiskCondition;
import com.stackwork360.riskengineservice.domain.RiskOperator;

public record RiskConditionResponse(
        String factKey,
        RiskOperator operator,
        String expectedValue
) {
    static RiskConditionResponse from(RiskCondition condition) {
        return new RiskConditionResponse(condition.factKey(), condition.operator(), condition.expectedValue());
    }
}
