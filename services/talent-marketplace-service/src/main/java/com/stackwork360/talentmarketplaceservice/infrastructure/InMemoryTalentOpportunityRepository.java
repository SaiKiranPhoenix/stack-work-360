package com.stackwork360.talentmarketplaceservice.infrastructure;

import com.stackwork360.talentmarketplaceservice.domain.TalentOpportunity;
import com.stackwork360.talentmarketplaceservice.domain.TalentOpportunityRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryTalentOpportunityRepository implements TalentOpportunityRepository {
    private final Map<UUID, TalentOpportunity> opportunities = new ConcurrentHashMap<>();

    public TalentOpportunity save(TalentOpportunity opportunity) {
        opportunities.put(opportunity.id(), opportunity);
        return opportunity;
    }

    public Optional<TalentOpportunity> findById(UUID id) {
        return Optional.ofNullable(opportunities.get(id));
    }

    public List<TalentOpportunity> findByTenantId(String tenantId) {
        return opportunities.values().stream()
                .filter(opportunity -> opportunity.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(TalentOpportunity::createdAt).reversed())
                .toList();
    }
}
