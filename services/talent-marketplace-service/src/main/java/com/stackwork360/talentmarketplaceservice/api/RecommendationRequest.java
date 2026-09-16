package com.stackwork360.talentmarketplaceservice.api;

import com.stackwork360.talentmarketplaceservice.application.RecommendationQuery;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record RecommendationRequest(
        @NotBlank String workerId,
        @Valid @NotEmpty List<CandidateSkillRequest> skills
) {
    RecommendationQuery toQuery(String tenantId) {
        return new RecommendationQuery(tenantId, workerId, skills.stream().map(CandidateSkillRequest::toDomain).toList());
    }
}
