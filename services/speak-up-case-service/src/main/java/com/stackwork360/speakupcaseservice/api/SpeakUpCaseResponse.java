package com.stackwork360.speakupcaseservice.api;

import com.stackwork360.speakupcaseservice.domain.SpeakUpCase;
import com.stackwork360.speakupcaseservice.domain.SpeakUpCaseCategory;
import com.stackwork360.speakupcaseservice.domain.SpeakUpCaseStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record SpeakUpCaseResponse(
        UUID id,
        SpeakUpCaseCategory category,
        String subject,
        String description,
        SpeakUpCaseStatus status,
        String assignedInvestigatorId,
        boolean legalHold,
        String legalMatterReference,
        List<EvidenceMetadataResponse> evidence,
        Instant updatedAt
) {
    public static SpeakUpCaseResponse from(SpeakUpCase speakUpCase) {
        return new SpeakUpCaseResponse(
                speakUpCase.id(),
                speakUpCase.category(),
                speakUpCase.subject(),
                speakUpCase.description(),
                speakUpCase.status(),
                speakUpCase.assignedInvestigatorId(),
                speakUpCase.legalHold(),
                speakUpCase.legalMatterReference(),
                speakUpCase.evidence().stream().map(EvidenceMetadataResponse::from).toList(),
                speakUpCase.updatedAt()
        );
    }
}
