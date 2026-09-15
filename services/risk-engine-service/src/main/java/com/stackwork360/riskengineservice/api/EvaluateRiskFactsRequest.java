package com.stackwork360.riskengineservice.api;

import com.stackwork360.riskengineservice.application.EvaluateRiskFactsCommand;
import com.stackwork360.riskengineservice.domain.RiskSource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record EvaluateRiskFactsRequest(
        @NotNull RiskSource source,
        @NotBlank String subjectId,
        Map<String, String> facts
) {
    EvaluateRiskFactsCommand toCommand(String tenantId) {
        return new EvaluateRiskFactsCommand(tenantId, source, subjectId, facts == null ? Map.of() : facts);
    }
}
