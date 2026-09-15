package com.stackwork360.riskengineservice.api;

import com.stackwork360.riskengineservice.application.CreateRiskRuleCommand;
import com.stackwork360.riskengineservice.domain.RiskSeverity;
import com.stackwork360.riskengineservice.domain.RiskSource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateRiskRuleRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull RiskSource source,
        @NotNull RiskSeverity severity,
        @NotEmpty List<@Valid RiskConditionRequest> conditions
) {
    CreateRiskRuleCommand toCommand(String tenantId) {
        return new CreateRiskRuleCommand(
                tenantId,
                name,
                description,
                source,
                severity,
                conditions.stream().map(RiskConditionRequest::toCommand).toList()
        );
    }
}
