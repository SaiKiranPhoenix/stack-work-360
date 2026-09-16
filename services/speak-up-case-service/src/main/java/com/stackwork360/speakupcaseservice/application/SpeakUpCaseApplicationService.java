package com.stackwork360.speakupcaseservice.application;

import com.stackwork360.speakupcaseservice.domain.CaseTimelineAction;
import com.stackwork360.speakupcaseservice.domain.CaseTimelineEntry;
import com.stackwork360.speakupcaseservice.domain.CaseTimelineRepository;
import com.stackwork360.speakupcaseservice.domain.EvidenceMetadata;
import com.stackwork360.speakupcaseservice.domain.SensitiveAuditAction;
import com.stackwork360.speakupcaseservice.domain.SensitiveAuditRecord;
import com.stackwork360.speakupcaseservice.domain.SensitiveAuditRepository;
import com.stackwork360.speakupcaseservice.domain.SpeakUpCase;
import com.stackwork360.speakupcaseservice.domain.SpeakUpCaseRepository;
import com.stackwork360.speakupcaseservice.domain.SpeakUpCaseStatus;
import com.stackwork360.web.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class SpeakUpCaseApplicationService {
    private final SpeakUpCaseRepository caseRepository;
    private final CaseTimelineRepository timelineRepository;
    private final SensitiveAuditRepository auditRepository;

    public SpeakUpCaseApplicationService(
            SpeakUpCaseRepository caseRepository,
            CaseTimelineRepository timelineRepository,
            SensitiveAuditRepository auditRepository
    ) {
        this.caseRepository = caseRepository;
        this.timelineRepository = timelineRepository;
        this.auditRepository = auditRepository;
    }

    public SpeakUpCase submit(SubmitCaseCommand command) {
        SpeakUpCase speakUpCase = caseRepository.save(new SpeakUpCase(
                null,
                command.tenantId(),
                command.reporterIdentityHash(),
                command.category(),
                command.subject(),
                command.description(),
                SpeakUpCaseStatus.SUBMITTED,
                null,
                false,
                null,
                List.of(),
                null
        ));
        timeline(speakUpCase, "anonymous", CaseTimelineAction.SUBMITTED, "Anonymous case submitted");
        audit(speakUpCase, "anonymous", SensitiveAuditAction.CASE_SUBMITTED, "anonymous portal submission");
        return speakUpCase;
    }

    public SpeakUpCase assignInvestigator(UUID caseId, AssignInvestigatorCommand command) {
        SpeakUpCase speakUpCase = get(caseId);
        speakUpCase.assignInvestigator(command.investigatorId());
        SpeakUpCase saved = caseRepository.save(speakUpCase);
        timeline(saved, command.actorId(), CaseTimelineAction.INVESTIGATOR_ASSIGNED, command.reason());
        audit(saved, command.actorId(), SensitiveAuditAction.INVESTIGATOR_ASSIGNED, command.reason());
        return saved;
    }

    public SpeakUpCase addEvidence(UUID caseId, AddEvidenceCommand command) {
        SpeakUpCase speakUpCase = get(caseId);
        speakUpCase.addEvidence(new EvidenceMetadata(
                null,
                command.fileName(),
                command.contentType(),
                command.sizeBytes(),
                command.storageReference(),
                command.actorId(),
                null
        ));
        SpeakUpCase saved = caseRepository.save(speakUpCase);
        timeline(saved, command.actorId(), CaseTimelineAction.EVIDENCE_ADDED, command.note());
        audit(saved, command.actorId(), SensitiveAuditAction.EVIDENCE_METADATA_ADDED, command.note());
        return saved;
    }

    public SpeakUpCase updateStatus(UUID caseId, UpdateCaseStatusCommand command) {
        SpeakUpCase speakUpCase = get(caseId);
        speakUpCase.moveTo(command.status());
        SpeakUpCase saved = caseRepository.save(speakUpCase);
        timeline(saved, command.actorId(), CaseTimelineAction.STATUS_CHANGED, command.reason());
        audit(saved, command.actorId(), SensitiveAuditAction.STATUS_CHANGED, command.reason());
        return saved;
    }

    public SpeakUpCase placeLegalHold(UUID caseId, PlaceLegalHoldCommand command) {
        SpeakUpCase speakUpCase = get(caseId);
        speakUpCase.placeLegalHold(command.legalMatterReference());
        SpeakUpCase saved = caseRepository.save(speakUpCase);
        timeline(saved, command.actorId(), CaseTimelineAction.LEGAL_HOLD_PLACED, command.reason());
        audit(saved, command.actorId(), SensitiveAuditAction.LEGAL_HOLD_PLACED, command.reason());
        return saved;
    }

    public SpeakUpCase get(UUID caseId) {
        SpeakUpCase speakUpCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new ResourceNotFoundException("speak-up case not found"));
        audit(speakUpCase, "system", SensitiveAuditAction.CASE_VIEWED, "case retrieved");
        return speakUpCase;
    }

    public List<SpeakUpCase> cases(String tenantId) {
        return caseRepository.findByTenantId(tenantId);
    }

    public List<CaseTimelineEntry> timeline(UUID caseId) {
        get(caseId);
        return timelineRepository.findByCaseId(caseId);
    }

    public List<SensitiveAuditRecord> auditTrail(UUID caseId) {
        get(caseId);
        return auditRepository.findByCaseId(caseId);
    }

    private void timeline(SpeakUpCase speakUpCase, String actorId, CaseTimelineAction action, String note) {
        timelineRepository.save(new CaseTimelineEntry(null, speakUpCase.id(), actorId, action, note, null));
    }

    private void audit(SpeakUpCase speakUpCase, String actorId, SensitiveAuditAction action, String reason) {
        auditRepository.save(new SensitiveAuditRecord(null, speakUpCase.tenantId(), speakUpCase.id(), actorId, action, reason, null));
    }
}
