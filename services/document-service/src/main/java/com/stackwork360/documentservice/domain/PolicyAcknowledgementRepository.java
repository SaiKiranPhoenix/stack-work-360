package com.stackwork360.documentservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PolicyAcknowledgementRepository {
    PolicyAcknowledgement save(PolicyAcknowledgement acknowledgement);

    Optional<PolicyAcknowledgement> findByDocumentIdAndWorkerId(UUID documentId, String workerId);

    List<PolicyAcknowledgement> findByTenantId(String tenantId);
}
