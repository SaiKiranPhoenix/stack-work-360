package com.stackwork360.speakupcaseservice.api;

import jakarta.validation.constraints.NotBlank;

public record PlaceLegalHoldRequest(
        @NotBlank String actorId,
        @NotBlank String legalMatterReference,
        @NotBlank String reason
) {
}
