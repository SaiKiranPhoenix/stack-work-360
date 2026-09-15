package com.stackwork360.helpdeskservice.api;

import jakarta.validation.constraints.NotBlank;

public record EscalateTicketRequest(
        @NotBlank
        String actorId,

        @NotBlank
        String reason
) {
}
