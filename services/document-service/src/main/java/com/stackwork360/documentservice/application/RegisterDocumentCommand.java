package com.stackwork360.documentservice.application;

import com.stackwork360.documentservice.domain.DocumentCategory;
import com.stackwork360.documentservice.domain.DocumentClassification;
import com.stackwork360.documentservice.domain.ObjectReference;
import com.stackwork360.documentservice.domain.RetentionPolicy;
import java.time.LocalDate;
import java.util.Set;

public record RegisterDocumentCommand(
        String tenantId,
        String subjectWorkerId,
        String title,
        DocumentCategory category,
        DocumentClassification classification,
        ObjectReference objectReference,
        LocalDate expiresOn,
        RetentionPolicy retentionPolicy,
        Set<String> allowedRoles
) {
}
