package com.stackwork360.developerintelligenceservice.api;

import jakarta.validation.constraints.NotBlank;

public record AccessDecisionRequest(
        @NotBlank String actorId
) {
}
