package com.stackwork360.accessgovernanceservice.api;

import com.stackwork360.accessgovernanceservice.application.CreateResourceCommand;
import com.stackwork360.accessgovernanceservice.domain.ResourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateResourceRequest(@NotBlank String resourceCode, @NotBlank String name, @NotNull ResourceType type, boolean privileged, @NotBlank String ownerId) {
    CreateResourceCommand toCommand(String tenantId) {
        return new CreateResourceCommand(tenantId, resourceCode, name, type, privileged, ownerId);
    }
}
