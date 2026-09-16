package com.stackwork360.accessgovernanceservice.application;

import com.stackwork360.accessgovernanceservice.domain.ResourceType;

public record CreateResourceCommand(String tenantId, String resourceCode, String name, ResourceType type, boolean privileged, String ownerId) {
}
