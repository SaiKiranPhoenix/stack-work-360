package com.stackwork360.speakupcaseservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.speakupcaseservice.application.AddEvidenceCommand;
import com.stackwork360.speakupcaseservice.application.AssignInvestigatorCommand;
import com.stackwork360.speakupcaseservice.application.PlaceLegalHoldCommand;
import com.stackwork360.speakupcaseservice.application.SpeakUpCaseApplicationService;
import com.stackwork360.speakupcaseservice.application.SubmitCaseCommand;
import com.stackwork360.speakupcaseservice.application.UpdateCaseStatusCommand;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/speak-up/v1/cases")
public class SpeakUpCaseController {
    private final SpeakUpCaseApplicationService speakUpCaseApplicationService;

    public SpeakUpCaseController(SpeakUpCaseApplicationService speakUpCaseApplicationService) {
        this.speakUpCaseApplicationService = speakUpCaseApplicationService;
    }

    @PostMapping
    public ResponseEntity<SpeakUpCaseResponse> submit(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody SubmitCaseRequest request
    ) {
        SpeakUpCaseResponse response = SpeakUpCaseResponse.from(speakUpCaseApplicationService.submit(new SubmitCaseCommand(
                tenantId,
                request.reporterIdentityHash(),
                request.category(),
                request.subject(),
                request.description()
        )));
        return ResponseEntity.created(URI.create("/api/speak-up/v1/cases/" + response.id())).body(response);
    }

    @GetMapping
    public List<SpeakUpCaseResponse> list(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return speakUpCaseApplicationService.cases(tenantId).stream().map(SpeakUpCaseResponse::from).toList();
    }

    @GetMapping("/{caseId}")
    public SpeakUpCaseResponse get(@PathVariable UUID caseId) {
        return SpeakUpCaseResponse.from(speakUpCaseApplicationService.get(caseId));
    }

    @PatchMapping("/{caseId}/investigator")
    public SpeakUpCaseResponse assignInvestigator(
            @PathVariable UUID caseId,
            @Valid @RequestBody AssignInvestigatorRequest request
    ) {
        return SpeakUpCaseResponse.from(speakUpCaseApplicationService.assignInvestigator(caseId, new AssignInvestigatorCommand(
                request.actorId(),
                request.investigatorId(),
                request.reason()
        )));
    }

    @PostMapping("/{caseId}/evidence")
    public SpeakUpCaseResponse addEvidence(
            @PathVariable UUID caseId,
            @Valid @RequestBody AddEvidenceRequest request
    ) {
        return SpeakUpCaseResponse.from(speakUpCaseApplicationService.addEvidence(caseId, new AddEvidenceCommand(
                request.actorId(),
                request.fileName(),
                request.contentType(),
                request.sizeBytes(),
                request.storageReference(),
                request.note()
        )));
    }

    @PatchMapping("/{caseId}/status")
    public SpeakUpCaseResponse updateStatus(
            @PathVariable UUID caseId,
            @Valid @RequestBody UpdateCaseStatusRequest request
    ) {
        return SpeakUpCaseResponse.from(speakUpCaseApplicationService.updateStatus(caseId, new UpdateCaseStatusCommand(
                request.actorId(),
                request.status(),
                request.reason()
        )));
    }

    @PatchMapping("/{caseId}/legal-hold")
    public SpeakUpCaseResponse placeLegalHold(
            @PathVariable UUID caseId,
            @Valid @RequestBody PlaceLegalHoldRequest request
    ) {
        return SpeakUpCaseResponse.from(speakUpCaseApplicationService.placeLegalHold(caseId, new PlaceLegalHoldCommand(
                request.actorId(),
                request.legalMatterReference(),
                request.reason()
        )));
    }

    @GetMapping("/{caseId}/timeline")
    public List<TimelineEntryResponse> timeline(@PathVariable UUID caseId) {
        return speakUpCaseApplicationService.timeline(caseId).stream().map(TimelineEntryResponse::from).toList();
    }

    @GetMapping("/{caseId}/sensitive-audit")
    public List<SensitiveAuditResponse> auditTrail(@PathVariable UUID caseId) {
        return speakUpCaseApplicationService.auditTrail(caseId).stream().map(SensitiveAuditResponse::from).toList();
    }
}
