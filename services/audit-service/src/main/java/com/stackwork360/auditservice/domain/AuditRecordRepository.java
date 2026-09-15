package com.stackwork360.auditservice.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuditRecordRepository {
    AuditRecord append(AuditRecord record);

    Optional<AuditRecord> findById(UUID id);

    List<AuditRecord> search(AuditQuery query);
}
