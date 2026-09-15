package com.stackwork360.riskengineservice.api;

import com.stackwork360.riskengineservice.application.UpdateRiskRuleCommand;
import com.stackwork360.riskengineservice.domain.RiskSeverity;
import com.stackwork360.riskengineservice.domain.RiskSource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateRiskRuleRequest(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull RiskSource source,
        @NotNull RiskSeverity severity,
        boolean enabled,
        @NotEmpty List<@Valid RiskConditionRequest> conditions
) {
    UpdateRiskRuleCommand toCommand() {
        return new UpdateRiskRuleCommand(
                name,
                description,
                source,
                severity,
                enabled,
                conditions.stream().map(RiskConditionRequest::toCommand).toList()
        );
    }
}
