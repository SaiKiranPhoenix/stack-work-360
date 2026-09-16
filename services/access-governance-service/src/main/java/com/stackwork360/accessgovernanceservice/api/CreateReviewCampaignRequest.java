package com.stackwork360.accessgovernanceservice.api;

import com.stackwork360.accessgovernanceservice.application.CreateReviewCampaignCommand;
import com.stackwork360.accessgovernanceservice.domain.ResourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateReviewCampaignRequest(@NotBlank String name, @NotNull ResourceType resourceType, @NotNull LocalDate dueOn) {
    CreateReviewCampaignCommand toCommand(String tenantId) {
        return new CreateReviewCampaignCommand(tenantId, name, resourceType, dueOn);
    }
}
