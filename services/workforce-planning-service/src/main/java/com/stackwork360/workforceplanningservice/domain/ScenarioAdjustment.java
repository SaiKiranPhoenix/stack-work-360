package com.stackwork360.workforceplanningservice.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record ScenarioAdjustment(
        String teamId,
        String location,
        int fullTimeDelta,
        int contractorDelta,
        BigDecimal annualCostDelta
) {
    public ScenarioAdjustment {
        teamId = requireText(teamId, "team id is required");
        location = requireText(location, "location is required");
        annualCostDelta = Objects.requireNonNull(annualCostDelta, "annual cost delta is required").setScale(2, RoundingMode.HALF_UP);
    }

    public int headcountDelta() {
        return fullTimeDelta + contractorDelta;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
