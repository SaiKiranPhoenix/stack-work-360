package com.stackwork360.talentmarketplaceservice.api;

import com.stackwork360.talentmarketplaceservice.domain.MobilityHistory;
import com.stackwork360.talentmarketplaceservice.domain.OpportunityType;
import java.time.Instant;
import java.util.UUID;

public record MobilityHistoryResponse(UUID id, String tenantId, String workerId, UUID opportunityId, OpportunityType opportunityType, String title, Instant recordedAt) {
    static MobilityHistoryResponse from(MobilityHistory history) {
        return new MobilityHistoryResponse(history.id(), history.tenantId(), history.workerId(), history.opportunityId(), history.opportunityType(), history.title(), history.recordedAt());
    }
}
