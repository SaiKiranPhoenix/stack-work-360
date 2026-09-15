package com.stackwork360.helpdeskservice.api;

import com.stackwork360.helpdeskservice.domain.TicketCategory;
import com.stackwork360.helpdeskservice.domain.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTicketRequest(
        @NotBlank
        String subject,

        @NotBlank
        String description,

        @NotNull
        TicketCategory category,

        @NotNull
        TicketPriority priority
) {
}
