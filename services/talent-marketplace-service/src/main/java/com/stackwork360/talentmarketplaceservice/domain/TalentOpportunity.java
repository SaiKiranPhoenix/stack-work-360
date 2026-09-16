package com.stackwork360.talentmarketplaceservice.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class TalentOpportunity {
    private final UUID id;
    private final String tenantId;
    private final OpportunityType type;
    private final String title;
    private final String ownerId;
    private final String teamId;
    private final List<RequiredSkill> requiredSkills;
    private final int capacity;
    private final LocalDate startsOn;
    private OpportunityStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    public TalentOpportunity(UUID id, String tenantId, OpportunityType type, String title, String ownerId, String teamId, List<RequiredSkill> requiredSkills, int capacity, LocalDate startsOn, OpportunityStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.type = Objects.requireNonNull(type, "opportunity type is required");
        this.title = requireText(title, "title is required");
        this.ownerId = requireText(ownerId, "owner id is required");
        this.teamId = requireText(teamId, "team id is required");
        this.requiredSkills = List.copyOf(Objects.requireNonNull(requiredSkills, "required skills are required"));
        if (this.requiredSkills.isEmpty()) {
            throw new IllegalArgumentException("opportunity requires at least one skill");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.capacity = capacity;
        this.startsOn = Objects.requireNonNull(startsOn, "start date is required");
        this.status = Objects.requireNonNull(status, "status is required");
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
        this.updatedAt = updatedAt == null ? this.createdAt : updatedAt;
    }

    public void open() {
        if (status != OpportunityStatus.DRAFT) {
            throw new IllegalStateException("only draft opportunities can be opened");
        }
        status = OpportunityStatus.OPEN;
        updatedAt = Instant.now();
    }

    public boolean openForApplications() {
        return status == OpportunityStatus.OPEN;
    }

    public UUID id() { return id; }
    public String tenantId() { return tenantId; }
    public OpportunityType type() { return type; }
    public String title() { return title; }
    public String ownerId() { return ownerId; }
    public String teamId() { return teamId; }
    public List<RequiredSkill> requiredSkills() { return requiredSkills; }
    public int capacity() { return capacity; }
    public LocalDate startsOn() { return startsOn; }
    public OpportunityStatus status() { return status; }
    public Instant createdAt() { return createdAt; }
    public Instant updatedAt() { return updatedAt; }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
