package com.stackwork360.speakupcaseservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.stackwork360.speakupcaseservice.api.SpeakUpCaseResponse;
import com.stackwork360.speakupcaseservice.domain.CaseTimelineAction;
import com.stackwork360.speakupcaseservice.domain.SensitiveAuditAction;
import com.stackwork360.speakupcaseservice.domain.SpeakUpCase;
import com.stackwork360.speakupcaseservice.domain.SpeakUpCaseCategory;
import com.stackwork360.speakupcaseservice.domain.SpeakUpCaseStatus;
import com.stackwork360.speakupcaseservice.infrastructure.InMemoryCaseTimelineRepository;
import com.stackwork360.speakupcaseservice.infrastructure.InMemorySensitiveAuditRepository;
import com.stackwork360.speakupcaseservice.infrastructure.InMemorySpeakUpCaseRepository;
import org.junit.jupiter.api.Test;

class SpeakUpCaseApplicationServiceTest {
    private final SpeakUpCaseApplicationService service = new SpeakUpCaseApplicationService(
            new InMemorySpeakUpCaseRepository(),
            new InMemoryCaseTimelineRepository(),
            new InMemorySensitiveAuditRepository()
    );

    @Test
    void submitsAnonymousCaseWithoutExposingReporterIdentityInResponse() {
        SpeakUpCase speakUpCase = submit();

        SpeakUpCaseResponse response = SpeakUpCaseResponse.from(speakUpCase);

        assertEquals(SpeakUpCaseStatus.SUBMITTED, response.status());
        assertFalse(response.toString().contains("reporter-hash-1"));
        assertTrue(service.timeline(speakUpCase.id()).stream()
                .anyMatch(entry -> entry.action() == CaseTimelineAction.SUBMITTED));
    }

    @Test
    void assignsInvestigatorAddsEvidenceAndMovesStatus() {
        SpeakUpCase speakUpCase = submit();

        service.assignInvestigator(speakUpCase.id(), new AssignInvestigatorCommand("hr-lead", "investigator-1", "restricted ethics queue"));
        service.addEvidence(speakUpCase.id(), new AddEvidenceCommand("investigator-1", "statement.pdf", "application/pdf", 4096, "vault://evidence/1", "Initial statement"));
        SpeakUpCase investigating = service.updateStatus(speakUpCase.id(), new UpdateCaseStatusCommand("investigator-1", SpeakUpCaseStatus.INVESTIGATING, "review started"));

        assertEquals(SpeakUpCaseStatus.INVESTIGATING, investigating.status());
        assertEquals(1, investigating.evidence().size());
        assertTrue(service.auditTrail(speakUpCase.id()).stream()
                .anyMatch(record -> record.action() == SensitiveAuditAction.EVIDENCE_METADATA_ADDED));
    }

    @Test
    void placesLegalHoldAndRecordsSensitiveAudit() {
        SpeakUpCase speakUpCase = submit();

        SpeakUpCase held = service.placeLegalHold(speakUpCase.id(), new PlaceLegalHoldCommand("legal-1", "MAT-2026-001", "preserve evidence"));

        assertTrue(held.legalHold());
        assertEquals("MAT-2026-001", held.legalMatterReference());
        assertTrue(service.auditTrail(speakUpCase.id()).stream()
                .anyMatch(record -> record.action() == SensitiveAuditAction.LEGAL_HOLD_PLACED));
    }

    private SpeakUpCase submit() {
        return service.submit(new SubmitCaseCommand(
                "tenant-1",
                "reporter-hash-1",
                SpeakUpCaseCategory.ETHICS,
                "Conflict of interest",
                "Anonymous report details"
        ));
    }
}
