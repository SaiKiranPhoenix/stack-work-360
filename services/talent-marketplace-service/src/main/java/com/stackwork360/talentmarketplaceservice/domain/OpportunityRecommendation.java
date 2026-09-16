package com.stackwork360.talentmarketplaceservice.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record OpportunityRecommendation(
        UUID opportunityId,
        OpportunityType type,
        String title,
        BigDecimal matchScore
) {
}
