package com.stackwork360.riskengineservice.api;

import com.stackwork360.riskengineservice.domain.RiskRule;
import com.stackwork360.riskengineservice.domain.RiskSeverity;
import com.stackwork360.riskengineservice.domain.RiskSource;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RiskRuleResponse(
        UUID id,
        String tenantId,
        String name,
        String description,
        RiskSource source,
        RiskSeverity severity,
        boolean enabled,
        List<RiskConditionResponse> conditions,
        Instant createdAt,
        Instant updatedAt
) {
    static RiskRuleResponse from(RiskRule rule) {
        return new RiskRuleResponse(
                rule.id(),
                rule.tenantId(),
                rule.name(),
                rule.description(),
                rule.source(),
                rule.severity(),
                rule.enabled(),
                rule.conditions().stream().map(RiskConditionResponse::from).toList(),
                rule.createdAt(),
                rule.updatedAt()
        );
    }
}
