package com.stackwork360.compensationservice.api;

import jakarta.validation.constraints.NotBlank;

public record DecisionRequest(
        @NotBlank String actorId
) {
}
