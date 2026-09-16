package com.stackwork360.speakupcaseservice.api;

import jakarta.validation.constraints.NotBlank;

public record AssignInvestigatorRequest(
        @NotBlank String actorId,
        @NotBlank String investigatorId,
        @NotBlank String reason
) {
}
