package com.stackwork360.developerintelligenceservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CodeRepositoryRepository {
    CodeRepository save(CodeRepository repository);

    Optional<CodeRepository> findById(UUID id);

    Optional<CodeRepository> findByTenantIdAndProviderAndExternalId(String tenantId, RepositoryProvider provider, String externalId);

    List<CodeRepository> findByTenantId(String tenantId);
}
