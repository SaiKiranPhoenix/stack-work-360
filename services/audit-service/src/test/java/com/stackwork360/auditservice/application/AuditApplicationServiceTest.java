package com.stackwork360.auditservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.stackwork360.auditservice.domain.AuditAction;
import com.stackwork360.auditservice.domain.AuditOutcome;
import com.stackwork360.auditservice.domain.AuditQuery;
import com.stackwork360.auditservice.domain.AuditSensitivity;
import com.stackwork360.auditservice.infrastructure.InMemoryAuditRecordRepository;
import com.stackwork360.auditservice.infrastructure.InMemoryAuditRetentionPolicyRepository;
import java.util.Map;
import org.junit.jupiter.api.Test;

class AuditApplicationServiceTest {
    private final AuditApplicationService service = new AuditApplicationService(
            new InMemoryAuditRecordRepository(),
            new InMemoryAuditRetentionPolicyRepository()
    );

    @Test
    void recordsSensitiveReadAsRestrictedSuccess() {
        var record = service.recordSensitiveRead(new SensitiveReadCommand(
                "tenant-1",
                "actor-1",
                "people-core",
                "worker",
                "worker-1",
                "Compensation audit",
                "corr-1",
                Map.of("field", "salary")
        ));

        assertEquals(AuditAction.SENSITIVE_READ, record.action());
        assertEquals(AuditOutcome.SUCCESS, record.outcome());
        assertEquals(AuditSensitivity.RESTRICTED, record.sensitivity());
    }

    @Test
    void exportsEvidenceFromFilteredQuery() {
        service.record(new RecordAuditCommand(
                "tenant-1",
                "actor-1",
                "leave-service",
                "leave-request",
                "leave-1",
                AuditAction.APPROVE,
                AuditOutcome.SUCCESS,
                AuditSensitivity.STANDARD,
                "Manager approval",
                "corr-1",
                Map.of()
        ));
        service.record(new RecordAuditCommand(
                "tenant-1",
                "actor-2",
                "document-service",
                "document",
                "doc-1",
                AuditAction.SENSITIVE_READ,
                AuditOutcome.SUCCESS,
                AuditSensitivity.RESTRICTED,
                "Legal review",
                "corr-2",
                Map.of()
        ));

        AuditEvidenceExport export = service.exportEvidence(new AuditQuery(
                "tenant-1",
                null,
                null,
                null,
                AuditAction.SENSITIVE_READ,
                AuditSensitivity.RESTRICTED,
                null,
                null
        ));

        assertEquals(1, export.recordCount());
        assertEquals("document-service", export.records().get(0).serviceName());
    }

    @Test
    void updatesRetentionPolicy() {
        var policy = service.updateRetentionPolicy(new UpdateRetentionPolicyCommand("tenant-1", 400, 2600, true));

        assertEquals(400, policy.standardRetentionDays());
        assertEquals(2600, policy.restrictedRetentionDays());
        assertEquals(true, policy.legalHoldEnabled());
    }
}
