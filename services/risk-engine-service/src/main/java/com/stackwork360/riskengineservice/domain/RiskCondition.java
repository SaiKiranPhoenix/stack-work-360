package com.stackwork360.riskengineservice.domain;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public record RiskCondition(
        String factKey,
        RiskOperator operator,
        String expectedValue
) {
    public RiskCondition {
        factKey = requireText(factKey, "fact key is required");
        operator = Objects.requireNonNull(operator, "operator is required");
        expectedValue = requireText(expectedValue, "expected value is required");
    }

    public boolean matches(Map<String, String> facts) {
        String actual = facts.get(factKey);
        if (actual == null) {
            return false;
        }
        return switch (operator) {
            case EQUALS -> actual.equalsIgnoreCase(expectedValue);
            case NOT_EQUALS -> !actual.equalsIgnoreCase(expectedValue);
            case GREATER_THAN -> decimal(actual).compareTo(decimal(expectedValue)) > 0;
            case GREATER_THAN_OR_EQUAL -> decimal(actual).compareTo(decimal(expectedValue)) >= 0;
            case LESS_THAN -> decimal(actual).compareTo(decimal(expectedValue)) < 0;
            case LESS_THAN_OR_EQUAL -> decimal(actual).compareTo(decimal(expectedValue)) <= 0;
            case CONTAINS -> actual.toLowerCase().contains(expectedValue.toLowerCase());
        };
    }

    public String explain() {
        return factKey + " " + operator.name().toLowerCase() + " " + expectedValue;
    }

    private static BigDecimal decimal(String value) {
        return new BigDecimal(value);
    }

    private static String requireText(String value, String message) {
        Objects.requireNonNull(value, message);
        if (value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}
