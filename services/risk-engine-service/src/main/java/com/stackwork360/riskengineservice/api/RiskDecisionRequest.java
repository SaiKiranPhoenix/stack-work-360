package com.stackwork360.riskengineservice.api;

import jakarta.validation.constraints.NotBlank;

public record RiskDecisionRequest(
        @NotBlank String actorId
) {
}
