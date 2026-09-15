package com.stackwork360.riskengineservice.api;

import com.stackwork360.riskengineservice.application.RiskExplanation;
import com.stackwork360.riskengineservice.domain.RiskSeverity;
import com.stackwork360.riskengineservice.domain.RiskSignalStatus;
import com.stackwork360.riskengineservice.domain.RiskSource;
import java.util.Map;
import java.util.UUID;

public record RiskExplanationResponse(
        UUID signalId,
        UUID ruleId,
        String ruleName,
        RiskSource source,
        String subjectId,
        RiskSeverity severity,
        RiskSignalStatus status,
        String explanation,
        Map<String, String> facts
) {
    static RiskExplanationResponse from(RiskExplanation explanation) {
        return new RiskExplanationResponse(
                explanation.signalId(),
                explanation.ruleId(),
                explanation.ruleName(),
                explanation.source(),
                explanation.subjectId(),
                explanation.severity(),
                explanation.status(),
                explanation.explanation(),
                explanation.facts()
        );
    }
}
