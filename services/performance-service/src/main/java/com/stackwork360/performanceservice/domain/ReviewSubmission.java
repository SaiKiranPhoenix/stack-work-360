package com.stackwork360.performanceservice.domain;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class ReviewSubmission {
    private final UUID id;
    private final String tenantId;
    private final UUID cycleId;
    private final String subjectWorkerId;
    private final String reviewerWorkerId;
    private final ReviewType type;
    private final int rating;
    private final String narrative;
    private final Map<String, Integer> competencyRatings;
    private ReviewStatus status;
    private Integer calibratedRating;
    private String calibrationNotes;
    private final Instant submittedAt;
    private Instant updatedAt;

    private ReviewSubmission(UUID id, String tenantId, UUID cycleId, String subjectWorkerId, String reviewerWorkerId, ReviewType type, int rating, String narrative, Map<String, Integer> competencyRatings, ReviewStatus status, Integer calibratedRating, String calibrationNotes, Instant submittedAt, Instant updatedAt) {
        this.id = Objects.requireNonNull(id, "review id is required");
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.cycleId = Objects.requireNonNull(cycleId, "cycle id is required");
        this.subjectWorkerId = requireText(subjectWorkerId, "subject worker id is required");
        this.reviewerWorkerId = requireText(reviewerWorkerId, "reviewer worker id is required");
        this.type = Objects.requireNonNull(type, "review type is required");
        this.rating = requireRating(rating);
        this.narrative = requireText(narrative, "narrative is required");
        this.competencyRatings = Map.copyOf(Objects.requireNonNull(competencyRatings, "competency ratings are required"));
        if (competencyRatings.isEmpty()) {
            throw new IllegalArgumentException("at least one competency rating is required");
        }
        competencyRatings.values().forEach(ReviewSubmission::requireRating);
        this.status = Objects.requireNonNull(status, "review status is required");
        this.calibratedRating = calibratedRating == null ? null : requireRating(calibratedRating);
        this.calibrationNotes = calibrationNotes;
        this.submittedAt = Objects.requireNonNull(submittedAt, "submitted at is required");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updated at is required");
    }

    public static ReviewSubmission submit(String tenantId, UUID cycleId, String subjectWorkerId, String reviewerWorkerId, ReviewType type, int rating, String narrative, Map<String, Integer> competencyRatings) {
        Instant now = Instant.now();
        return new ReviewSubmission(UUID.randomUUID(), tenantId, cycleId, subjectWorkerId, reviewerWorkerId, type, rating, narrative, competencyRatings, ReviewStatus.SUBMITTED, null, null, now, now);
    }

    public void calibrate(int calibratedRating, String notes) {
        if (type != ReviewType.MANAGER) {
            throw new IllegalStateException("only manager reviews can be calibrated");
        }
        if (status != ReviewStatus.SUBMITTED) {
            throw new IllegalStateException("only submitted reviews can be calibrated");
        }
        this.calibratedRating = requireRating(calibratedRating);
        this.calibrationNotes = requireText(notes, "calibration notes are required");
        this.status = ReviewStatus.CALIBRATED;
        this.updatedAt = Instant.now();
    }

    public void makeVisible() {
        if (type == ReviewType.PEER) {
            throw new IllegalStateException("peer feedback is not directly visible to employees");
        }
        if (status != ReviewStatus.SUBMITTED && status != ReviewStatus.CALIBRATED) {
            throw new IllegalStateException("only submitted or calibrated reviews can be made visible");
        }
        status = ReviewStatus.VISIBLE;
        updatedAt = Instant.now();
    }

    public int finalRating() {
        return calibratedRating == null ? rating : calibratedRating;
    }

    public UUID id() { return id; }
    public String tenantId() { return tenantId; }
    public UUID cycleId() { return cycleId; }
    public String subjectWorkerId() { return subjectWorkerId; }
    public String reviewerWorkerId() { return reviewerWorkerId; }
    public ReviewType type() { return type; }
    public int rating() { return rating; }
    public String narrative() { return narrative; }
    public Map<String, Integer> competencyRatings() { return competencyRatings; }
    public ReviewStatus status() { return status; }
    public Integer calibratedRating() { return calibratedRating; }
    public String calibrationNotes() { return calibrationNotes; }
    public Instant submittedAt() { return submittedAt; }
    public Instant updatedAt() { return updatedAt; }

    private static int requireRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("rating must be between 1 and 5");
        }
        return rating;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
