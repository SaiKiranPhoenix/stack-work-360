package com.stackwork360.helpdeskservice.api;

import jakarta.validation.constraints.NotBlank;

public record AssignTicketRequest(
        @NotBlank
        String assigneeId
) {
}
