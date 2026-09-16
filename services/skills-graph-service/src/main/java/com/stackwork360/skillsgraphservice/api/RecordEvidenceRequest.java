package com.stackwork360.skillsgraphservice.api;

import com.stackwork360.skillsgraphservice.application.RecordEvidenceCommand;
import com.stackwork360.skillsgraphservice.domain.EvidenceType;
import com.stackwork360.skillsgraphservice.domain.ProficiencyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecordEvidenceRequest(
        @NotBlank String workerId,
        @NotBlank String skillCode,
        @NotNull EvidenceType type,
        @NotBlank String sourceId,
        @NotNull ProficiencyLevel level
) {
    RecordEvidenceCommand toCommand(String tenantId) {
        return new RecordEvidenceCommand(tenantId, workerId, skillCode, type, sourceId, level);
    }
}
