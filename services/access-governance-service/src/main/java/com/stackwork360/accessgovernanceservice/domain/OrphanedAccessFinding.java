package com.stackwork360.accessgovernanceservice.domain;

import java.util.UUID;

public record OrphanedAccessFinding(
        UUID accessRequestId,
        String requesterId,
        String resourceCode,
        String reason
) {
}
