package com.stackwork360.benefitsservice.domain;

import java.util.List;
import java.util.Optional;

public interface BenefitPlanRepository {
    BenefitPlan save(BenefitPlan plan);
    Optional<BenefitPlan> findByCode(String tenantId, String planCode);
    List<BenefitPlan> findByTenantId(String tenantId);
}
