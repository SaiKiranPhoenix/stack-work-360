package com.stackwork360.talentmarketplaceservice.application;

import com.stackwork360.talentmarketplaceservice.domain.CandidateSkill;
import java.util.List;

public record RecommendationQuery(String tenantId, String workerId, List<CandidateSkill> skills) {
}
