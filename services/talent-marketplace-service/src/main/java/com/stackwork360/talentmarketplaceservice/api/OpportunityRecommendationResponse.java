package com.stackwork360.talentmarketplaceservice.api;

import com.stackwork360.talentmarketplaceservice.domain.OpportunityRecommendation;
import com.stackwork360.talentmarketplaceservice.domain.OpportunityType;
import java.math.BigDecimal;
import java.util.UUID;

public record OpportunityRecommendationResponse(UUID opportunityId, OpportunityType type, String title, BigDecimal matchScore) {
    static OpportunityRecommendationResponse from(OpportunityRecommendation recommendation) {
        return new OpportunityRecommendationResponse(recommendation.opportunityId(), recommendation.type(), recommendation.title(), recommendation.matchScore());
    }
}
