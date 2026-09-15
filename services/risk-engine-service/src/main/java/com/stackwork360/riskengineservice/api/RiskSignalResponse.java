package com.stackwork360.riskengineservice.api;

import com.stackwork360.riskengineservice.domain.RiskSeverity;
import com.stackwork360.riskengineservice.domain.RiskSignal;
import com.stackwork360.riskengineservice.domain.RiskSignalStatus;
import com.stackwork360.riskengineservice.domain.RiskSource;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record RiskSignalResponse(
        UUID id,
        String tenantId,
        RiskSource source,
        String subjectId,
        UUID ruleId,
        String ruleName,
        RiskSeverity severity,
        String explanation,
        Map<String, String> facts,
        RiskSignalStatus status,
        String resolvedBy,
        Instant resolvedAt,
        Instant createdAt,
        Instant updatedAt
) {
    static RiskSignalResponse from(RiskSignal signal) {
        return new RiskSignalResponse(
                signal.id(),
                signal.tenantId(),
                signal.source(),
                signal.subjectId(),
                signal.ruleId(),
                signal.ruleName(),
                signal.severity(),
                signal.explanation(),
                signal.facts(),
                signal.status(),
                signal.resolvedBy(),
                signal.resolvedAt(),
                signal.createdAt(),
                signal.updatedAt()
        );
    }
}
