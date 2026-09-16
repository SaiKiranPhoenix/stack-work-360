package com.stackwork360.talentmarketplaceservice.api;

import com.stackwork360.talentmarketplaceservice.domain.OpportunityStatus;
import com.stackwork360.talentmarketplaceservice.domain.OpportunityType;
import com.stackwork360.talentmarketplaceservice.domain.RequiredSkill;
import com.stackwork360.talentmarketplaceservice.domain.TalentOpportunity;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record TalentOpportunityResponse(UUID id, String tenantId, OpportunityType type, String title, String ownerId, String teamId, List<RequiredSkill> requiredSkills, int capacity, LocalDate startsOn, OpportunityStatus status, Instant createdAt, Instant updatedAt) {
    static TalentOpportunityResponse from(TalentOpportunity opportunity) {
        return new TalentOpportunityResponse(opportunity.id(), opportunity.tenantId(), opportunity.type(), opportunity.title(), opportunity.ownerId(), opportunity.teamId(), opportunity.requiredSkills(), opportunity.capacity(), opportunity.startsOn(), opportunity.status(), opportunity.createdAt(), opportunity.updatedAt());
    }
}
