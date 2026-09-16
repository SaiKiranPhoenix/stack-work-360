package com.stackwork360.benefitsservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BenefitChangeRequestRepository {
    BenefitChangeRequest save(BenefitChangeRequest request);
    Optional<BenefitChangeRequest> findById(UUID id);
    List<BenefitChangeRequest> findByTenantId(String tenantId);
}
