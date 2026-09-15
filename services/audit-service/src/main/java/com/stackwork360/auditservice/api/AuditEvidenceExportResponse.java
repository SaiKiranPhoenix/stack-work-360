package com.stackwork360.auditservice.api;

import com.stackwork360.auditservice.application.AuditEvidenceExport;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AuditEvidenceExportResponse(
        UUID exportId,
        String tenantId,
        Instant generatedAt,
        int recordCount,
        List<AuditRecordResponse> records
) {
    static AuditEvidenceExportResponse from(AuditEvidenceExport export) {
        return new AuditEvidenceExportResponse(
                export.exportId(),
                export.tenantId(),
                export.generatedAt(),
                export.recordCount(),
                export.records().stream().map(AuditRecordResponse::from).toList()
        );
    }
}
