package com.stackwork360.benefitsservice.domain;

import java.util.List;

public interface EligibilityRuleRepository {
    EligibilityRule save(EligibilityRule rule);
    List<EligibilityRule> findByPlanCode(String tenantId, String planCode);
    List<EligibilityRule> findByTenantId(String tenantId);
}
