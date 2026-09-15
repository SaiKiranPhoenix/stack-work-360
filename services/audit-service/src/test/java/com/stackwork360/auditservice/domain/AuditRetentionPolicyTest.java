package com.stackwork360.auditservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AuditRetentionPolicyTest {
    @Test
    void returnsRestrictedRetentionForRestrictedRecords() {
        AuditRetentionPolicy policy = AuditRetentionPolicy.defaults("tenant-1");
        policy.update(365, 2555, false);

        assertEquals(2555, policy.retentionDaysFor(AuditSensitivity.RESTRICTED));
        assertEquals(365, policy.retentionDaysFor(AuditSensitivity.STANDARD));
    }

    @Test
    void legalHoldRetainsIndefinitely() {
        AuditRetentionPolicy policy = AuditRetentionPolicy.defaults("tenant-1");
        policy.update(365, 2555, true);

        assertEquals(Integer.MAX_VALUE, policy.retentionDaysFor(AuditSensitivity.CONFIDENTIAL));
    }

    @Test
    void rejectsRestrictedRetentionShorterThanStandard() {
        AuditRetentionPolicy policy = AuditRetentionPolicy.defaults("tenant-1");

        assertThrows(IllegalArgumentException.class, () -> policy.update(365, 90, false));
    }
}
