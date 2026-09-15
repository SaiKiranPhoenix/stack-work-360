package com.stackwork360.auditservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.Test;

class AuditRecordTest {
    @Test
    void createsImmutableAuditRecordWithMetadataSnapshot() {
        Map<String, String> metadata = new java.util.HashMap<>();
        metadata.put("field", "salary");

        AuditRecord record = AuditRecord.record(
                "tenant-1",
                "actor-1",
                "people-core",
                "worker",
                "worker-1",
                AuditAction.SENSITIVE_READ,
                AuditOutcome.SUCCESS,
                AuditSensitivity.RESTRICTED,
                "Payroll review",
                "corr-1",
                metadata
        );
        metadata.put("field", "changed");

        assertEquals("salary", record.metadata().get("field"));
        assertThrows(UnsupportedOperationException.class, () -> record.metadata().put("x", "y"));
    }
}
