package com.stackwork360.goalsokrservice.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Goal {
    private final UUID id;
    private final String tenantId;
    private final String ownerId;
    private final String teamId;
    private final GoalLevel level;
    private final String title;
    private final UUID parentGoalId;
    private final LocalDate startsOn;
    private final LocalDate dueOn;
    private GoalStatus status;
    private final List<Objective> objectives;

    public Goal(UUID id, String tenantId, String ownerId, String teamId, GoalLevel level, String title, UUID parentGoalId, LocalDate startsOn, LocalDate dueOn, GoalStatus status, List<Objective> objectives) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.tenantId = requireText(tenantId, "tenant id is required");
        this.ownerId = requireText(ownerId, "owner id is required");
        this.teamId = requireText(teamId, "team id is required");
        this.level = Objects.requireNonNull(level, "goal level is required");
        this.title = requireText(title, "goal title is required");
        this.parentGoalId = parentGoalId;
        this.startsOn = Objects.requireNonNull(startsOn, "start date is required");
        this.dueOn = Objects.requireNonNull(dueOn, "due date is required");
        if (dueOn.isBefore(startsOn)) {
            throw new IllegalArgumentException("goal due date cannot be before start date");
        }
        this.status = Objects.requireNonNull(status, "goal status is required");
        this.objectives = new ArrayList<>(Objects.requireNonNull(objectives, "objectives are required"));
        if (this.objectives.isEmpty()) {
            throw new IllegalArgumentException("goal requires at least one objective");
        }
    }

    public void activate() {
        if (status != GoalStatus.DRAFT) {
            throw new IllegalStateException("only draft goals can be activated");
        }
        status = GoalStatus.ACTIVE;
    }

    public void updateKeyResult(UUID keyResultId, BigDecimal currentValue) {
        objectives.stream()
                .filter(objective -> objective.keyResults().stream().anyMatch(keyResult -> keyResult.id().equals(keyResultId)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("key result not found"))
                .keyResult(keyResultId)
                .update(currentValue);
        if (progress().value().compareTo(new BigDecimal("100")) == 0) {
            status = GoalStatus.COMPLETED;
        } else if (progress().value().compareTo(new BigDecimal("40")) < 0 && LocalDate.now().isAfter(startsOn.plusDays(14))) {
            status = GoalStatus.AT_RISK;
        }
    }

    public ProgressScore progress() {
        BigDecimal total = objectives.stream()
                .map(objective -> objective.progress().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new ProgressScore(total.divide(BigDecimal.valueOf(objectives.size()), 2, RoundingMode.HALF_UP));
    }

    public UUID id() { return id; }
    public String tenantId() { return tenantId; }
    public String ownerId() { return ownerId; }
    public String teamId() { return teamId; }
    public GoalLevel level() { return level; }
    public String title() { return title; }
    public UUID parentGoalId() { return parentGoalId; }
    public LocalDate startsOn() { return startsOn; }
    public LocalDate dueOn() { return dueOn; }
    public GoalStatus status() { return status; }
    public List<Objective> objectives() { return List.copyOf(objectives); }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
