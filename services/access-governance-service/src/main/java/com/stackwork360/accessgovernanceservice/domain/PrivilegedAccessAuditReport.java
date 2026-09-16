package com.stackwork360.accessgovernanceservice.domain;

import java.util.List;

public record PrivilegedAccessAuditReport(
        String tenantId,
        int privilegedGrantCount,
        List<String> requesterIds
) {
}
