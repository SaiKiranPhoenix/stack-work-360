package com.stackwork360.riskengineservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RiskSignalRepository {
    RiskSignal save(RiskSignal signal);

    Optional<RiskSignal> findById(UUID id);

    List<RiskSignal> findByTenantId(String tenantId);

    List<RiskSignal> findByTenantIdAndSubjectId(String tenantId, String subjectId);
}
