package com.stackwork360.documentservice.application;

import com.stackwork360.documentservice.domain.DocumentMetadata;
import com.stackwork360.documentservice.domain.DocumentRepository;
import com.stackwork360.documentservice.domain.PolicyAcknowledgement;
import com.stackwork360.documentservice.domain.PolicyAcknowledgementRepository;
import com.stackwork360.web.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class DocumentApplicationService {
    private final DocumentRepository documentRepository;
    private final PolicyAcknowledgementRepository acknowledgementRepository;

    public DocumentApplicationService(
            DocumentRepository documentRepository,
            PolicyAcknowledgementRepository acknowledgementRepository
    ) {
        this.documentRepository = documentRepository;
        this.acknowledgementRepository = acknowledgementRepository;
    }

    public DocumentMetadata register(RegisterDocumentCommand command) {
        DocumentMetadata document = DocumentMetadata.register(
                command.tenantId(),
                command.subjectWorkerId(),
                command.title(),
                command.category(),
                command.classification(),
                command.objectReference(),
                command.expiresOn(),
                command.retentionPolicy(),
                command.allowedRoles()
        );
        return documentRepository.save(document);
    }

    public DocumentMetadata update(UUID documentId, UpdateDocumentCommand command) {
        DocumentMetadata document = get(documentId);
        document.updateMetadata(
                command.title(),
                command.category(),
                command.classification(),
                command.expiresOn(),
                command.retentionPolicy(),
                command.allowedRoles()
        );
        return documentRepository.save(document);
    }

    public DocumentMetadata getReadable(UUID documentId, Set<String> roles, String workerId) {
        DocumentMetadata document = get(documentId);
        if (!document.canBeReadBy(roles, workerId)) {
            throw new IllegalStateException("actor is not allowed to read this document");
        }
        return document;
    }

    public DocumentMetadata get(UUID documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("document not found"));
    }

    public List<DocumentMetadata> list(String tenantId) {
        return documentRepository.findByTenantId(tenantId);
    }

    public List<DocumentMetadata> listForWorker(String tenantId, String workerId) {
        return documentRepository.findByTenantIdAndSubjectWorkerId(tenantId, workerId);
    }

    public PolicyAcknowledgement acknowledge(AcknowledgePolicyCommand command) {
        DocumentMetadata document = get(command.documentId());
        if (!document.tenantId().equals(command.tenantId())) {
            throw new IllegalArgumentException("document belongs to another tenant");
        }
        if (document.category().name().equals("POLICY")) {
            return acknowledgementRepository.save(new PolicyAcknowledgement(
                    null,
                    command.tenantId(),
                    command.documentId(),
                    command.workerId(),
                    null,
                    command.ipAddress()
            ));
        }
        throw new IllegalArgumentException("only policy documents can be acknowledged");
    }

    public List<PolicyAcknowledgement> acknowledgements(String tenantId) {
        return acknowledgementRepository.findByTenantId(tenantId);
    }

    public List<DocumentMetadata> expired(String tenantId, LocalDate date) {
        return documentRepository.findByTenantId(tenantId).stream()
                .filter(document -> document.expiredOn(date))
                .toList();
    }
}
