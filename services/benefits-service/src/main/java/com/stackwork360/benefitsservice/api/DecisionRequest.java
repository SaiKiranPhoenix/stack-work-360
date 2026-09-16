package com.stackwork360.benefitsservice.api;

import jakarta.validation.constraints.NotBlank;

public record DecisionRequest(
        @NotBlank String actorId
) {
}
