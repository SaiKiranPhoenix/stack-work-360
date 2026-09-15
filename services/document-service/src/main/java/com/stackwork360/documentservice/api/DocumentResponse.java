package com.stackwork360.documentservice.api;

import com.stackwork360.documentservice.domain.DocumentCategory;
import com.stackwork360.documentservice.domain.DocumentClassification;
import com.stackwork360.documentservice.domain.DocumentMetadata;
import com.stackwork360.documentservice.domain.ObjectReference;
import com.stackwork360.documentservice.domain.RetentionPolicy;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        String subjectWorkerId,
        String title,
        DocumentCategory category,
        DocumentClassification classification,
        ObjectReference objectReference,
        LocalDate expiresOn,
        RetentionPolicy retentionPolicy,
        Set<String> allowedRoles,
        boolean legalHold,
        Instant createdAt,
        Instant updatedAt
) {
    public static DocumentResponse from(DocumentMetadata document) {
        return new DocumentResponse(
                document.id(),
                document.subjectWorkerId(),
                document.title(),
                document.category(),
                document.classification(),
                document.objectReference(),
                document.expiresOn(),
                document.retentionPolicy(),
                document.allowedRoles(),
                document.legalHold(),
                document.createdAt(),
                document.updatedAt()
        );
    }
}
