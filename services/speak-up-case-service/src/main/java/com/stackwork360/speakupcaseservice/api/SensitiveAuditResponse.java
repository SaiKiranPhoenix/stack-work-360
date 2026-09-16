package com.stackwork360.speakupcaseservice.api;

import com.stackwork360.speakupcaseservice.domain.SensitiveAuditAction;
import com.stackwork360.speakupcaseservice.domain.SensitiveAuditRecord;
import java.time.Instant;
import java.util.UUID;

public record SensitiveAuditResponse(
        UUID id,
        UUID caseId,
        String actorId,
        SensitiveAuditAction action,
        String reason,
        Instant occurredAt
) {
    public static SensitiveAuditResponse from(SensitiveAuditRecord record) {
        return new SensitiveAuditResponse(record.id(), record.caseId(), record.actorId(), record.action(), record.reason(), record.occurredAt());
    }
}
