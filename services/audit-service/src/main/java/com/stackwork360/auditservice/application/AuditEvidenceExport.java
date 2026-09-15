package com.stackwork360.auditservice.application;

import com.stackwork360.auditservice.domain.AuditRecord;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AuditEvidenceExport(
        UUID exportId,
        String tenantId,
        Instant generatedAt,
        int recordCount,
        List<AuditRecord> records
) {
    public static AuditEvidenceExport create(String tenantId, List<AuditRecord> records) {
        return new AuditEvidenceExport(UUID.randomUUID(), tenantId, Instant.now(), records.size(), List.copyOf(records));
    }
}
