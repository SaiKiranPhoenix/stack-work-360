package com.stackwork360.accessgovernanceservice.application;

import com.stackwork360.accessgovernanceservice.domain.AccessLevel;

public record RequestAccessCommand(String tenantId, String requesterId, String resourceCode, AccessLevel level, String justification) {
}
