package com.stackwork360.accessgovernanceservice.domain;

import java.util.List;

public interface AccessReviewCampaignRepository {
    AccessReviewCampaign save(AccessReviewCampaign campaign);
    List<AccessReviewCampaign> findByTenantId(String tenantId);
}
