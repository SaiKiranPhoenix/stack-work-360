package com.stackwork360.talentmarketplaceservice.application;

import com.stackwork360.talentmarketplaceservice.domain.OpportunityType;
import com.stackwork360.talentmarketplaceservice.domain.RequiredSkill;
import java.time.LocalDate;
import java.util.List;

public record CreateOpportunityCommand(String tenantId, OpportunityType type, String title, String ownerId, String teamId, List<RequiredSkill> requiredSkills, int capacity, LocalDate startsOn) {
}
