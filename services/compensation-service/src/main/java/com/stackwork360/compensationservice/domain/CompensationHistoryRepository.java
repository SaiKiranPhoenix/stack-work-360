package com.stackwork360.compensationservice.domain;

import java.util.List;
import java.util.Optional;

public interface CompensationHistoryRepository {
    CompensationHistory save(CompensationHistory history);
    Optional<CompensationHistory> latestForWorker(String tenantId, String workerId);
    List<CompensationHistory> findByTenantId(String tenantId);
    List<CompensationHistory> findByWorker(String tenantId, String workerId);
}
