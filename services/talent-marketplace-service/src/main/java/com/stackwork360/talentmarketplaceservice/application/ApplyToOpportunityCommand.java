package com.stackwork360.talentmarketplaceservice.application;

import java.util.UUID;

public record ApplyToOpportunityCommand(String tenantId, UUID opportunityId, String workerId, String managerId, String note) {
}
