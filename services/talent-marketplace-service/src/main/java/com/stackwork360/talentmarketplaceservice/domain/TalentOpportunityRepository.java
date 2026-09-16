package com.stackwork360.talentmarketplaceservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TalentOpportunityRepository {
    TalentOpportunity save(TalentOpportunity opportunity);
    Optional<TalentOpportunity> findById(UUID id);
    List<TalentOpportunity> findByTenantId(String tenantId);
}
