package com.stackwork360.talentmarketplaceservice.api;

import com.stackwork360.talentmarketplaceservice.application.ApplyToOpportunityCommand;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record ApplyToOpportunityRequest(
        @NotBlank String workerId,
        @NotBlank String managerId,
        @NotBlank String note
) {
    ApplyToOpportunityCommand toCommand(String tenantId, UUID opportunityId) {
        return new ApplyToOpportunityCommand(tenantId, opportunityId, workerId, managerId, note);
    }
}
