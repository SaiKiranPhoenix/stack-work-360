package com.stackwork360.accessgovernanceservice.application;

import com.stackwork360.accessgovernanceservice.domain.ResourceType;
import java.time.LocalDate;

public record CreateReviewCampaignCommand(String tenantId, String name, ResourceType resourceType, LocalDate dueOn) {
}
