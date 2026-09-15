package com.stackwork360.shiftschedulingservice.api;

import jakarta.validation.constraints.NotBlank;

public record ShiftDecisionRequest(
        @NotBlank String actorId
) {
}
