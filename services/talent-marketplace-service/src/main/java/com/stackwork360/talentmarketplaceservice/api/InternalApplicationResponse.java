package com.stackwork360.talentmarketplaceservice.api;

import com.stackwork360.talentmarketplaceservice.domain.ApplicationStatus;
import com.stackwork360.talentmarketplaceservice.domain.InternalApplication;
import java.time.Instant;
import java.util.UUID;

public record InternalApplicationResponse(UUID id, String tenantId, UUID opportunityId, String workerId, String managerId, String note, ApplicationStatus status, String decisionReason, Instant appliedAt, Instant updatedAt) {
    static InternalApplicationResponse from(InternalApplication application) {
        return new InternalApplicationResponse(application.id(), application.tenantId(), application.opportunityId(), application.workerId(), application.managerId(), application.note(), application.status(), application.decisionReason(), application.appliedAt(), application.updatedAt());
    }
}
