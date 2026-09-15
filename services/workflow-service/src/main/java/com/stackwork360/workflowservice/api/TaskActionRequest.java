package com.stackwork360.workflowservice.api;

import jakarta.validation.constraints.NotBlank;

public record TaskActionRequest(
        @NotBlank
        String actorId
) {
}
