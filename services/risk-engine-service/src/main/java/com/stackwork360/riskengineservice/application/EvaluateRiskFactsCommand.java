package com.stackwork360.riskengineservice.application;

import com.stackwork360.riskengineservice.domain.RiskSource;
import java.util.Map;

public record EvaluateRiskFactsCommand(
        String tenantId,
        RiskSource source,
        String subjectId,
        Map<String, String> facts
) {
}
