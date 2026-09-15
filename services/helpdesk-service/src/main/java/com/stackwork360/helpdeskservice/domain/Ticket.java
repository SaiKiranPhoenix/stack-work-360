package com.stackwork360.helpdeskservice.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Ticket {
    private final UUID id;
    private final String tenantId;
    private final String requesterId;
    private String subject;
    private String description;
    private TicketCategory category;
    private TicketPriority priority;
    private TicketStatus status;
    private String assigneeGroup;
    private String assigneeId;
    private Instant dueAt;
    private final List<TicketComment> comments;
    private final Instant createdAt;
    private Instant updatedAt;

    private Ticket(
            UUID id,
            String tenantId,
            String requesterId,
            String subject,
            String description,
            TicketCategory category,
            TicketPriority priority,
            TicketStatus status,
            String assigneeGroup,
            String assigneeId,
            Instant dueAt,
            List<TicketComment> comments,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "ticket id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.requesterId = requireText(requesterId, "requester id is required");
        this.subject = requireText(subject, "ticket subject is required");
        this.description = requireText(description, "ticket description is required");
        this.category = Objects.requireNonNull(category, "ticket category is required");
        this.priority = Objects.requireNonNull(priority, "ticket priority is required");
        this.status = Objects.requireNonNull(status, "ticket status is required");
        this.assigneeGroup = requireText(assigneeGroup, "assignee group is required");
        this.assigneeId = blankToNull(assigneeId);
        this.dueAt = Objects.requireNonNull(dueAt, "due at is required");
        this.comments = new ArrayList<>(comments == null ? List.of() : comments);
        this.createdAt = Objects.requireNonNull(createdAt, "created at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static Ticket create(
            String tenantId,
            String requesterId,
            String subject,
            String description,
            TicketCategory category,
            TicketPriority priority
    ) {
        Instant now = Instant.now();
        return new Ticket(
                UUID.randomUUID(),
                tenantId,
                requesterId,
                subject,
                description,
                category,
                priority,
                TicketStatus.OPEN,
                TicketRoutingPolicy.assigneeGroupFor(category),
                null,
                now.plus(TicketRoutingPolicy.slaFor(priority)),
                List.of(),
                now,
                now
        );
    }

    public void update(String subject, String description, TicketCategory category, TicketPriority priority) {
        ensureMutable();
        this.subject = requireText(subject, "ticket subject is required");
        this.description = requireText(description, "ticket description is required");
        this.category = Objects.requireNonNull(category, "ticket category is required");
        this.priority = Objects.requireNonNull(priority, "ticket priority is required");
        this.assigneeGroup = TicketRoutingPolicy.assigneeGroupFor(category);
        this.dueAt = Instant.now().plus(TicketRoutingPolicy.slaFor(priority));
        this.updatedAt = Instant.now();
    }

    public void assignTo(String assigneeId) {
        ensureMutable();
        this.assigneeId = requireText(assigneeId, "assignee id is required");
        this.status = TicketStatus.IN_PROGRESS;
        this.updatedAt = Instant.now();
    }

    public void addComment(TicketComment comment) {
        ensureMutable();
        comments.add(Objects.requireNonNull(comment, "comment is required"));
        updatedAt = Instant.now();
    }

    public void escalate(String actorId, String reason) {
        ensureMutable();
        this.status = TicketStatus.ESCALATED;
        this.priority = TicketPriority.URGENT;
        this.dueAt = Instant.now().plus(TicketRoutingPolicy.slaFor(TicketPriority.URGENT));
        comments.add(new TicketComment(null, actorId, "Escalated: " + requireText(reason, "escalation reason is required"), true, List.of(), null));
        updatedAt = Instant.now();
    }

    public void resolve(String actorId, String resolution) {
        ensureMutable();
        this.status = TicketStatus.RESOLVED;
        comments.add(new TicketComment(null, actorId, "Resolution: " + requireText(resolution, "resolution is required"), false, List.of(), null));
        updatedAt = Instant.now();
    }

    public void close() {
        if (status != TicketStatus.RESOLVED) {
            throw new IllegalStateException("only resolved tickets can be closed");
        }
        this.status = TicketStatus.CLOSED;
        updatedAt = Instant.now();
    }

    public boolean overdueAt(Instant now) {
        return status != TicketStatus.RESOLVED && status != TicketStatus.CLOSED && dueAt.isBefore(now);
    }

    public UUID id() {
        return id;
    }

    public String tenantId() {
        return tenantId;
    }

    public String requesterId() {
        return requesterId;
    }

    public String subject() {
        return subject;
    }

    public String description() {
        return description;
    }

    public TicketCategory category() {
        return category;
    }

    public TicketPriority priority() {
        return priority;
    }

    public TicketStatus status() {
        return status;
    }

    public String assigneeGroup() {
        return assigneeGroup;
    }

    public String assigneeId() {
        return assigneeId;
    }

    public Instant dueAt() {
        return dueAt;
    }

    public List<TicketComment> comments() {
        return comments.stream()
                .sorted(Comparator.comparing(TicketComment::createdAt))
                .toList();
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    private void ensureMutable() {
        if (status == TicketStatus.CLOSED) {
            throw new IllegalStateException("closed tickets cannot be changed");
        }
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
