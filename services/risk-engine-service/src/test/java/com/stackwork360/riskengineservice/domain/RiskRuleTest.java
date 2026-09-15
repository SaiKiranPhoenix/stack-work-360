package com.stackwork360.riskengineservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class RiskRuleTest {
    @Test
    void matchesFactsWhenAllConditionsPass() {
        RiskRule rule = rule();
        RiskFacts facts = new RiskFacts(
                "tenant-1",
                RiskSource.DEVELOPER_OWNERSHIP,
                "repo-1",
                Map.of("busFactor", "1", "lifecycle", "ACTIVE")
        );

        assertTrue(rule.matches(facts));
    }

    @Test
    void doesNotMatchWrongSource() {
        RiskRule rule = rule();
        RiskFacts facts = new RiskFacts(
                "tenant-1",
                RiskSource.LEAVE,
                "repo-1",
                Map.of("busFactor", "1", "lifecycle", "ACTIVE")
        );

        assertFalse(rule.matches(facts));
    }

    @Test
    void createsExplainableSignal() {
        RiskRule rule = rule();
        RiskFacts facts = new RiskFacts(
                "tenant-1",
                RiskSource.DEVELOPER_OWNERSHIP,
                "repo-1",
                Map.of("busFactor", "1", "lifecycle", "ACTIVE")
        );

        RiskSignal signal = rule.toSignal(facts);

        assertEquals(RiskSeverity.HIGH, signal.severity());
        assertEquals(RiskSignalStatus.OPEN, signal.status());
        assertTrue(signal.explanation().contains("Single maintainer repository"));
    }

    @Test
    void requiresAtLeastOneCondition() {
        assertThrows(IllegalArgumentException.class, () -> RiskRule.create(
                "tenant-1",
                "Invalid",
                "No conditions",
                RiskSource.MANUAL,
                RiskSeverity.LOW,
                List.of()
        ));
    }

    private static RiskRule rule() {
        return RiskRule.create(
                "tenant-1",
                "Single maintainer repository",
                "Repository has too few maintainers",
                RiskSource.DEVELOPER_OWNERSHIP,
                RiskSeverity.HIGH,
                List.of(
                        new RiskCondition("busFactor", RiskOperator.LESS_THAN, "2"),
                        new RiskCondition("lifecycle", RiskOperator.EQUALS, "ACTIVE")
                )
        );
    }
}
