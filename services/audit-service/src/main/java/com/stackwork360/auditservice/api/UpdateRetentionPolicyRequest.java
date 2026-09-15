package com.stackwork360.auditservice.api;

import com.stackwork360.auditservice.application.UpdateRetentionPolicyCommand;
import jakarta.validation.constraints.Min;

public record UpdateRetentionPolicyRequest(
        @Min(30) int standardRetentionDays,
        @Min(30) int restrictedRetentionDays,
        boolean legalHoldEnabled
) {
    UpdateRetentionPolicyCommand toCommand(String tenantId) {
        return new UpdateRetentionPolicyCommand(tenantId, standardRetentionDays, restrictedRetentionDays, legalHoldEnabled);
    }
}
