package com.stackwork360.riskengineservice.application;

import com.stackwork360.riskengineservice.domain.RiskSeverity;
import com.stackwork360.riskengineservice.domain.RiskSignalStatus;
import com.stackwork360.riskengineservice.domain.RiskSource;
import java.util.Map;
import java.util.UUID;

public record RiskExplanation(
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
}
