package com.stackwork360.documentservice.api;

import com.stackwork360.documentservice.domain.DocumentCategory;
import com.stackwork360.documentservice.domain.DocumentClassification;
import com.stackwork360.documentservice.domain.RetentionPolicy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public record UpdateDocumentRequest(
        @NotBlank
        String title,

        @NotNull
        DocumentCategory category,

        @NotNull
        DocumentClassification classification,

        LocalDate expiresOn,

        @Valid
        @NotNull
        RetentionPolicy retentionPolicy,

        Set<String> allowedRoles
) {
}
