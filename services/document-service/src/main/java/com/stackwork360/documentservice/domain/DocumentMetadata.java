package com.stackwork360.documentservice.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class DocumentMetadata {
    private final UUID id;
    private final String tenantId;
    private String subjectWorkerId;
    private String title;
    private DocumentCategory category;
    private DocumentClassification classification;
    private ObjectReference objectReference;
    private LocalDate expiresOn;
    private RetentionPolicy retentionPolicy;
    private Set<String> allowedRoles;
    private boolean legalHold;
    private final Instant createdAt;
    private Instant updatedAt;

    private DocumentMetadata(
            UUID id,
            String tenantId,
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
        this.id = Objects.requireNonNull(id, "document id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.subjectWorkerId = blankToNull(subjectWorkerId);
        this.title = requireText(title, "document title is required");
        this.category = Objects.requireNonNull(category, "document category is required");
        this.classification = Objects.requireNonNull(classification, "document classification is required");
        this.objectReference = Objects.requireNonNull(objectReference, "object reference is required");
        this.expiresOn = expiresOn;
        this.retentionPolicy = Objects.requireNonNull(retentionPolicy, "retention policy is required");
        this.allowedRoles = Set.copyOf(allowedRoles == null ? Set.of() : allowedRoles);
        this.legalHold = legalHold;
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static DocumentMetadata register(
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
        Instant now = Instant.now();
        return new DocumentMetadata(
                UUID.randomUUID(),
                tenantId,
                subjectWorkerId,
                title,
                category,
                classification,
                objectReference,
                expiresOn,
                retentionPolicy,
                allowedRoles,
                false,
                now,
                now
        );
    }

    public void updateMetadata(
            String title,
            DocumentCategory category,
            DocumentClassification classification,
            LocalDate expiresOn,
            RetentionPolicy retentionPolicy,
            Set<String> allowedRoles
    ) {
        this.title = requireText(title, "document title is required");
        this.category = Objects.requireNonNull(category, "document category is required");
        this.classification = Objects.requireNonNull(classification, "document classification is required");
        this.expiresOn = expiresOn;
        this.retentionPolicy = Objects.requireNonNull(retentionPolicy, "retention policy is required");
        this.allowedRoles = Set.copyOf(allowedRoles == null ? Set.of() : allowedRoles);
        this.updatedAt = Instant.now();
    }

    public boolean canBeReadBy(Set<String> roles, String workerId) {
        if (classification == DocumentClassification.INTERNAL) {
            return true;
        }
        if (subjectWorkerId != null && subjectWorkerId.equals(workerId)) {
            return true;
        }
        return roles != null && roles.stream().anyMatch(allowedRoles::contains);
    }

    public boolean expiredOn(LocalDate date) {
        return expiresOn != null && !expiresOn.isAfter(date);
    }

    public boolean eligibleForDeletion(LocalDate date) {
        return !legalHold && retentionPolicy.deleteAfter().isBefore(date);
    }

    public void placeLegalHold() {
        legalHold = true;
        updatedAt = Instant.now();
    }

    public void releaseLegalHold() {
        legalHold = false;
        updatedAt = Instant.now();
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public String subjectWorkerId() {
        return subjectWorkerId;
    }

    public String title() {
        return title;
    }

    public DocumentCategory category() {
        return category;
    }

    public DocumentClassification classification() {
        return classification;
    }

    public ObjectReference objectReference() {
        return objectReference;
    }

    public LocalDate expiresOn() {
        return expiresOn;
    }

    public RetentionPolicy retentionPolicy() {
        return retentionPolicy;
    }

    public Set<String> allowedRoles() {
        return allowedRoles;
    }

    public boolean legalHold() {
        return legalHold;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
