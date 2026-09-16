package com.stackwork360.goalsokrservice.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Objective {
    private final UUID id;
    private final String title;
    private final List<KeyResult> keyResults;

    public Objective(UUID id, String title, List<KeyResult> keyResults) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.title = requireText(title, "objective title is required");
        this.keyResults = new ArrayList<>(Objects.requireNonNull(keyResults, "key results are required"));
        if (this.keyResults.isEmpty()) {
            throw new IllegalArgumentException("objective requires at least one key result");
        }
    }

    public ProgressScore progress() {
        BigDecimal total = keyResults.stream()
                .map(keyResult -> keyResult.progress().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new ProgressScore(total.divide(BigDecimal.valueOf(keyResults.size()), 2, RoundingMode.HALF_UP));
    }

    public UUID id() { return id; }
    public String title() { return title; }
    public List<KeyResult> keyResults() { return List.copyOf(keyResults); }

    public KeyResult keyResult(UUID keyResultId) {
        return keyResults.stream()
                .filter(keyResult -> keyResult.id().equals(keyResultId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("key result not found"));
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
