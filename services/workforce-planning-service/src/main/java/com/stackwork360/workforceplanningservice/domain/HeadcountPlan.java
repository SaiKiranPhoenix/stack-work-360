package com.stackwork360.workforceplanningservice.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;

public record HeadcountPlan(
        UUID id,
        String tenantId,
        String teamId,
        String location,
        int currentFullTime,
        int currentContractors,
        int targetFullTime,
        int targetContractors,
        BigDecimal averageFullTimeCost,
        BigDecimal averageContractorCost
) {
    public HeadcountPlan {
        id = id == null ? UUID.randomUUID() : id;
        tenantId = requireText(tenantId, "tenant id is required");
        teamId = requireText(teamId, "team id is required");
        location = requireText(location, "location is required");
        if (currentFullTime < 0 || currentContractors < 0 || targetFullTime < 0 || targetContractors < 0) {
            throw new IllegalArgumentException("headcount values cannot be negative");
        }
        averageFullTimeCost = scalePositive(averageFullTimeCost, "average full-time cost is required");
        averageContractorCost = scalePositive(averageContractorCost, "average contractor cost is required");
    }

    public int targetHeadcount() {
        return targetFullTime + targetContractors;
    }

    public BigDecimal targetCost() {
        return averageFullTimeCost.multiply(BigDecimal.valueOf(targetFullTime))
                .add(averageContractorCost.multiply(BigDecimal.valueOf(targetContractors)))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal scalePositive(BigDecimal value, String message) {
        value = Objects.requireNonNull(value, message).setScale(2, RoundingMode.HALF_UP);
        if (value.signum() < 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
