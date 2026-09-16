package com.stackwork360.speakupcaseservice.domain;

import java.util.List;
import java.util.UUID;

public interface SensitiveAuditRepository {
    SensitiveAuditRecord save(SensitiveAuditRecord record);
    List<SensitiveAuditRecord> findByCaseId(UUID caseId);
}
