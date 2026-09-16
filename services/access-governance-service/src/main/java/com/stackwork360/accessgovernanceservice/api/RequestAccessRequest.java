package com.stackwork360.accessgovernanceservice.api;

import com.stackwork360.accessgovernanceservice.application.RequestAccessCommand;
import com.stackwork360.accessgovernanceservice.domain.AccessLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestAccessRequest(@NotBlank String requesterId, @NotBlank String resourceCode, @NotNull AccessLevel level, @NotBlank String justification) {
    RequestAccessCommand toCommand(String tenantId) {
        return new RequestAccessCommand(tenantId, requesterId, resourceCode, level, justification);
    }
}
