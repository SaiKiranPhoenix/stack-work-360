package com.stackwork360.accessgovernanceservice.api;

import com.stackwork360.accessgovernanceservice.application.CreatePolicyCommand;
import com.stackwork360.accessgovernanceservice.domain.AccessLevel;
import com.stackwork360.accessgovernanceservice.domain.ResourceType;
import jakarta.validation.constraints.NotNull;

public record CreatePolicyRequest(@NotNull ResourceType resourceType, @NotNull AccessLevel minimumLevel, boolean managerApprovalRequired, boolean ownerApprovalRequired, boolean securityApprovalRequired) {
    CreatePolicyCommand toCommand(String tenantId) {
        return new CreatePolicyCommand(tenantId, resourceType, minimumLevel, managerApprovalRequired, ownerApprovalRequired, securityApprovalRequired);
    }
}
