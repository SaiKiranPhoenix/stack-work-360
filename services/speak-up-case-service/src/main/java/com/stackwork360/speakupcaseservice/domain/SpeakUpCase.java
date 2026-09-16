package com.stackwork360.speakupcaseservice.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class SpeakUpCase {
    private final UUID id;
    private final String tenantId;
    private final String reporterIdentityHash;
    private final SpeakUpCaseCategory category;
    private final String subject;
    private final String description;
    private final List<EvidenceMetadata> evidence;
    private SpeakUpCaseStatus status;
    private String assignedInvestigatorId;
    private boolean legalHold;
    private String legalMatterReference;
    private Instant updatedAt;

    public SpeakUpCase(
            UUID id,
            String tenantId,
            String reporterIdentityHash,
            SpeakUpCaseCategory category,
            String subject,
            String description,
            SpeakUpCaseStatus status,
            String assignedInvestigatorId,
            boolean legalHold,
            String legalMatterReference,
            List<EvidenceMetadata> evidence,
            Instant updatedAt
    ) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.reporterIdentityHash = requireText(reporterIdentityHash, "reporter identity hash is required");
        this.category = Objects.requireNonNull(category, "category is required");
        this.subject = requireText(subject, "subject is required");
        this.description = requireText(description, "description is required");
        this.status = Objects.requireNonNull(status, "status is required");
        this.assignedInvestigatorId = assignedInvestigatorId;
        this.legalHold = legalHold;
        this.legalMatterReference = legalMatterReference;
        this.evidence = new ArrayList<>(evidence == null ? List.of() : evidence);
        this.updatedAt = updatedAt == null ? Instant.now() : updatedAt;
    }

    public void assignInvestigator(String investigatorId) {
        String investigator = requireText(investigatorId, "investigator id is required");
        if (investigator.equals(reporterIdentityHash)) {
            throw new IllegalStateException("reporter identity cannot be assigned as investigator");
        }
        assignedInvestigatorId = investigator;
        if (status == SpeakUpCaseStatus.SUBMITTED) {
            status = SpeakUpCaseStatus.TRIAGED;
        }
        updatedAt = Instant.now();
    }

    public void addEvidence(EvidenceMetadata metadata) {
        evidence.add(Objects.requireNonNull(metadata, "evidence metadata is required"));
        updatedAt = Instant.now();
    }

    public void moveTo(SpeakUpCaseStatus nextStatus) {
        Objects.requireNonNull(nextStatus, "next status is required");
        if (nextStatus == SpeakUpCaseStatus.INVESTIGATING && assignedInvestigatorId == null) {
            throw new IllegalStateException("case requires an investigator before investigation");
        }
        if (status == SpeakUpCaseStatus.CLOSED) {
            throw new IllegalStateException("closed cases cannot change status");
        }
        if (nextStatus.ordinal() < status.ordinal()) {
            throw new IllegalStateException("case status cannot move backward");
        }
        status = nextStatus;
        updatedAt = Instant.now();
    }

    public void placeLegalHold(String matterReference) {
        legalHold = true;
        legalMatterReference = requireText(matterReference, "legal matter reference is required");
        updatedAt = Instant.now();
    }

    public UUID id() { return id; }
    public String tenantId() { return tenantId; }
    public String reporterIdentityHash() { return reporterIdentityHash; }
    public SpeakUpCaseCategory category() { return category; }
    public String subject() { return subject; }
    public String description() { return description; }
    public SpeakUpCaseStatus status() { return status; }
    public String assignedInvestigatorId() { return assignedInvestigatorId; }
    public boolean legalHold() { return legalHold; }
    public String legalMatterReference() { return legalMatterReference; }
    public List<EvidenceMetadata> evidence() { return List.copyOf(evidence); }
    public Instant updatedAt() { return updatedAt; }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
