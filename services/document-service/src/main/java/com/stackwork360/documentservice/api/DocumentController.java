package com.stackwork360.documentservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.documentservice.application.AcknowledgePolicyCommand;
import com.stackwork360.documentservice.application.DocumentApplicationService;
import com.stackwork360.documentservice.application.RegisterDocumentCommand;
import com.stackwork360.documentservice.application.UpdateDocumentCommand;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/documents/v1")
public class DocumentController {
    private final DocumentApplicationService documentApplicationService;

    public DocumentController(DocumentApplicationService documentApplicationService) {
        this.documentApplicationService = documentApplicationService;
    }

    @PostMapping("/documents")
    public ResponseEntity<DocumentResponse> register(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody RegisterDocumentRequest request
    ) {
        DocumentResponse response = DocumentResponse.from(documentApplicationService.register(new RegisterDocumentCommand(
                tenantId,
                request.subjectWorkerId(),
                request.title(),
                request.category(),
                request.classification(),
                request.objectReference(),
                request.expiresOn(),
                request.retentionPolicy(),
                request.allowedRoles()
        )));
        return ResponseEntity.created(URI.create("/api/documents/v1/documents/" + response.id()))
                .body(response);
    }

    @GetMapping("/documents")
    public List<DocumentResponse> list(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return documentApplicationService.list(tenantId).stream()
                .map(DocumentResponse::from)
                .toList();
    }

    @GetMapping("/workers/{workerId}/documents")
    public List<DocumentResponse> listForWorker(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable String workerId
    ) {
        return documentApplicationService.listForWorker(tenantId, workerId).stream()
                .map(DocumentResponse::from)
                .toList();
    }

    @GetMapping("/documents/{documentId}")
    public DocumentResponse getReadable(
            @PathVariable UUID documentId,
            @RequestParam(required = false, defaultValue = "") String workerId,
            @RequestParam(required = false, defaultValue = "") String roles
    ) {
        return DocumentResponse.from(documentApplicationService.getReadable(documentId, roles(roles), workerId));
    }

    @PutMapping("/documents/{documentId}")
    public DocumentResponse update(
            @PathVariable UUID documentId,
            @Valid @RequestBody UpdateDocumentRequest request
    ) {
        return DocumentResponse.from(documentApplicationService.update(documentId, new UpdateDocumentCommand(
                request.title(),
                request.category(),
                request.classification(),
                request.expiresOn(),
                request.retentionPolicy(),
                request.allowedRoles()
        )));
    }

    @PostMapping("/documents/{documentId}/acknowledgements")
    public PolicyAcknowledgementResponse acknowledge(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable UUID documentId,
            @Valid @RequestBody AcknowledgePolicyRequest request
    ) {
        return PolicyAcknowledgementResponse.from(documentApplicationService.acknowledge(new AcknowledgePolicyCommand(
                tenantId,
                documentId,
                request.workerId(),
                request.ipAddress()
        )));
    }

    @GetMapping("/acknowledgements")
    public List<PolicyAcknowledgementResponse> acknowledgements(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId
    ) {
        return documentApplicationService.acknowledgements(tenantId).stream()
                .map(PolicyAcknowledgementResponse::from)
                .toList();
    }

    @GetMapping("/documents/expired")
    public List<DocumentResponse> expired(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return documentApplicationService.expired(tenantId, date).stream()
                .map(DocumentResponse::from)
                .toList();
    }

    private static Set<String> roles(String roles) {
        if (roles == null || roles.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(roles.split(","))
                .map(String::trim)
                .filter(role -> !role.isBlank())
                .collect(Collectors.toUnmodifiableSet());
    }
}
