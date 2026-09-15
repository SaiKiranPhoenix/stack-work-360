package com.stackwork360.documentservice.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DocumentMetadataTest {
    @Test
    void allowsRestrictedDocumentForAllowedRole() {
        DocumentMetadata document = restrictedDocument();

        assertTrue(document.canBeReadBy(Set.of("HR_OPS"), "other-worker"));
    }

    @Test
    void allowsRestrictedDocumentForSubjectWorker() {
        DocumentMetadata document = restrictedDocument();

        assertTrue(document.canBeReadBy(Set.of(), "worker-1"));
    }

    @Test
    void rejectsRestrictedDocumentForUnrelatedWorkerWithoutRole() {
        DocumentMetadata document = restrictedDocument();

        assertFalse(document.canBeReadBy(Set.of("EMPLOYEE"), "worker-2"));
    }

    @Test
    void respectsRetentionAndLegalHold() {
        DocumentMetadata document = restrictedDocument();

        assertTrue(document.eligibleForDeletion(LocalDate.of(2030, 1, 2)));

        document.placeLegalHold();

        assertFalse(document.eligibleForDeletion(LocalDate.of(2030, 1, 2)));
    }

    private static DocumentMetadata restrictedDocument() {
        return DocumentMetadata.register(
                "tenant-1",
                "worker-1",
                "Contract",
                DocumentCategory.CONTRACT,
                DocumentClassification.RESTRICTED,
                new ObjectReference("documents", "tenant-1/contract.pdf", "abc123", 1024, "application/pdf"),
                LocalDate.of(2030, 1, 1),
                new RetentionPolicy(LocalDate.of(2030, 1, 1), "Employment document"),
                Set.of("HR_OPS")
        );
    }
}
