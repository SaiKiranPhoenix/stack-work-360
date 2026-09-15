package com.stackwork360.documentservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.stackwork360.documentservice.domain.DocumentCategory;
import com.stackwork360.documentservice.domain.DocumentClassification;
import com.stackwork360.documentservice.domain.DocumentMetadata;
import com.stackwork360.documentservice.domain.ObjectReference;
import com.stackwork360.documentservice.domain.PolicyAcknowledgement;
import com.stackwork360.documentservice.domain.RetentionPolicy;
import com.stackwork360.documentservice.infrastructure.InMemoryDocumentRepository;
import com.stackwork360.documentservice.infrastructure.InMemoryPolicyAcknowledgementRepository;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DocumentApplicationServiceTest {
    private final DocumentApplicationService service = new DocumentApplicationService(
            new InMemoryDocumentRepository(),
            new InMemoryPolicyAcknowledgementRepository()
    );

    @Test
    void registersDocumentMetadata() {
        DocumentMetadata document = service.register(command(DocumentCategory.CONTRACT, DocumentClassification.RESTRICTED));

        assertEquals("Employment contract", document.title());
        assertEquals(1, service.list("tenant-1").size());
    }

    @Test
    void enforcesRestrictedReadAccess() {
        DocumentMetadata document = service.register(command(DocumentCategory.CONTRACT, DocumentClassification.RESTRICTED));

        assertThrows(IllegalStateException.class, () -> service.getReadable(document.id(), Set.of("EMPLOYEE"), "worker-2"));
    }

    @Test
    void acknowledgesPolicyDocument() {
        DocumentMetadata document = service.register(command(DocumentCategory.POLICY, DocumentClassification.CONFIDENTIAL));

        PolicyAcknowledgement acknowledgement = service.acknowledge(new AcknowledgePolicyCommand(
                "tenant-1",
                document.id(),
                "worker-1",
                "127.0.0.1"
        ));

        assertEquals("worker-1", acknowledgement.workerId());
        assertEquals(1, service.acknowledgements("tenant-1").size());
    }

    @Test
    void rejectsAcknowledgementForNonPolicyDocument() {
        DocumentMetadata document = service.register(command(DocumentCategory.CONTRACT, DocumentClassification.CONFIDENTIAL));

        assertThrows(IllegalArgumentException.class, () -> service.acknowledge(new AcknowledgePolicyCommand(
                "tenant-1",
                document.id(),
                "worker-1",
                "127.0.0.1"
        )));
    }

    private static RegisterDocumentCommand command(
            DocumentCategory category,
            DocumentClassification classification
    ) {
        return new RegisterDocumentCommand(
                "tenant-1",
                "worker-1",
                category == DocumentCategory.POLICY ? "Security policy" : "Employment contract",
                category,
                classification,
                new ObjectReference("documents", "tenant-1/file.pdf", "abc123", 2048, "application/pdf"),
                LocalDate.of(2030, 1, 1),
                new RetentionPolicy(LocalDate.of(2035, 1, 1), "HR document"),
                Set.of("HR_OPS")
        );
    }
}
