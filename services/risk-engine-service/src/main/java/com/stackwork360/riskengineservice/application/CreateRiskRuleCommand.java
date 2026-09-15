package com.stackwork360.riskengineservice.application;

import com.stackwork360.riskengineservice.domain.RiskSeverity;
import com.stackwork360.riskengineservice.domain.RiskSource;
import java.util.List;

public record CreateRiskRuleCommand(
        String tenantId,
        String name,
        String description,
        RiskSource source,
        RiskSeverity severity,
        List<RiskConditionCommand> conditions
) {
}
