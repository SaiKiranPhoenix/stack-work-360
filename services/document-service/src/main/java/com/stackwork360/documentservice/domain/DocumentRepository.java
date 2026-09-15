package com.stackwork360.documentservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository {
    DocumentMetadata save(DocumentMetadata document);

    Optional<DocumentMetadata> findById(UUID id);

    List<DocumentMetadata> findByTenantId(String tenantId);

    List<DocumentMetadata> findByTenantIdAndSubjectWorkerId(String tenantId, String workerId);
}
