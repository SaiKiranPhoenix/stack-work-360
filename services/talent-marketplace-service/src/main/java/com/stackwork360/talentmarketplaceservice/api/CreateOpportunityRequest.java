package com.stackwork360.talentmarketplaceservice.api;

import com.stackwork360.talentmarketplaceservice.application.CreateOpportunityCommand;
import com.stackwork360.talentmarketplaceservice.domain.OpportunityType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record CreateOpportunityRequest(
        @NotNull OpportunityType type,
        @NotBlank String title,
        @NotBlank String ownerId,
        @NotBlank String teamId,
        @Valid @NotEmpty List<RequiredSkillRequest> requiredSkills,
        @Min(1) int capacity,
        @NotNull LocalDate startsOn
) {
    CreateOpportunityCommand toCommand(String tenantId) {
        return new CreateOpportunityCommand(tenantId, type, title, ownerId, teamId, requiredSkills.stream().map(RequiredSkillRequest::toDomain).toList(), capacity, startsOn);
    }
}
