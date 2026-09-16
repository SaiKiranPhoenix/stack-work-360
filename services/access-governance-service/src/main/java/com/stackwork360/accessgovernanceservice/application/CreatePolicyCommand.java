package com.stackwork360.accessgovernanceservice.application;

import com.stackwork360.accessgovernanceservice.domain.AccessLevel;
import com.stackwork360.accessgovernanceservice.domain.ResourceType;

public record CreatePolicyCommand(String tenantId, ResourceType resourceType, AccessLevel minimumLevel, boolean managerApprovalRequired, boolean ownerApprovalRequired, boolean securityApprovalRequired) {
}
