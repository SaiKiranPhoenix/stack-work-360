package com.stackwork360.skillsgraphservice.domain;

public enum ProficiencyLevel {
    AWARE(1),
    WORKING(2),
    PROFICIENT(3),
    ADVANCED(4),
    EXPERT(5);

    private final int score;

    ProficiencyLevel(int score) {
        this.score = score;
    }

    public int score() {
        return score;
    }

    public boolean atLeast(ProficiencyLevel required) {
        return score >= required.score;
    }
}
