package com.stackwork360.auditservice.application;

public record UpdateRetentionPolicyCommand(
        String tenantId,
        int standardRetentionDays,
        int restrictedRetentionDays,
        boolean legalHoldEnabled
) {
}
