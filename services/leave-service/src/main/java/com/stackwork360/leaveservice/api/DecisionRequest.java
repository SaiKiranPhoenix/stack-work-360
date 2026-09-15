package com.stackwork360.leaveservice.api;

import jakarta.validation.constraints.NotBlank;

public record DecisionRequest(
        @NotBlank
        String actorId
) {
}
