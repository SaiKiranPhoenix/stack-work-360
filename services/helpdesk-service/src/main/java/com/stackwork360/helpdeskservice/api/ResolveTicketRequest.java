package com.stackwork360.helpdeskservice.api;

import jakarta.validation.constraints.NotBlank;

public record ResolveTicketRequest(
        @NotBlank
        String actorId,

        @NotBlank
        String resolution
) {
}
