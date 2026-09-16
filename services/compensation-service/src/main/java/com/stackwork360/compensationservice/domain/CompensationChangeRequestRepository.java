package com.stackwork360.compensationservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompensationChangeRequestRepository {
    CompensationChangeRequest save(CompensationChangeRequest request);
    Optional<CompensationChangeRequest> findById(UUID id);
    List<CompensationChangeRequest> findByTenantId(String tenantId);
}
