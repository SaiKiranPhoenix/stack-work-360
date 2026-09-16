package com.stackwork360.talentmarketplaceservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InternalApplicationRepository {
    InternalApplication save(InternalApplication application);
    Optional<InternalApplication> findById(UUID id);
    List<InternalApplication> findByWorker(String tenantId, String workerId);
    List<InternalApplication> findByTenantId(String tenantId);
}
