package com.stackwork360.skillsgraphservice.application;

import com.stackwork360.skillsgraphservice.domain.EvidenceType;
import com.stackwork360.skillsgraphservice.domain.ProficiencyLevel;

public record RecordEvidenceCommand(String tenantId, String workerId, String skillCode, EvidenceType type, String sourceId, ProficiencyLevel level) {
}
