package com.stackwork360.riskengineservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RiskRuleRepository {
    RiskRule save(RiskRule rule);

    Optional<RiskRule> findById(UUID id);

    List<RiskRule> findByTenantId(String tenantId);

    List<RiskRule> findByTenantIdAndSource(String tenantId, RiskSource source);
}
