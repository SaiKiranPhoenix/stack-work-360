package com.stackwork360.accessgovernanceservice.infrastructure;

import com.stackwork360.accessgovernanceservice.domain.AccessReviewCampaign;
import com.stackwork360.accessgovernanceservice.domain.AccessReviewCampaignRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAccessReviewCampaignRepository implements AccessReviewCampaignRepository {
    private final Map<UUID, AccessReviewCampaign> campaigns = new ConcurrentHashMap<>();

    public AccessReviewCampaign save(AccessReviewCampaign campaign) {
        campaigns.put(campaign.id(), campaign);
        return campaign;
    }

    public List<AccessReviewCampaign> findByTenantId(String tenantId) {
        return campaigns.values().stream()
                .filter(campaign -> campaign.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(AccessReviewCampaign::dueOn))
                .toList();
    }
}
