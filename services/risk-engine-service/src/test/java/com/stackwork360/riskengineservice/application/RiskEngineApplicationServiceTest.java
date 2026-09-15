package com.stackwork360.riskengineservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.stackwork360.riskengineservice.domain.RiskOperator;
import com.stackwork360.riskengineservice.domain.RiskSeverity;
import com.stackwork360.riskengineservice.domain.RiskSignal;
import com.stackwork360.riskengineservice.domain.RiskSignalStatus;
import com.stackwork360.riskengineservice.domain.RiskSource;
import com.stackwork360.riskengineservice.infrastructure.InMemoryRiskRuleRepository;
import com.stackwork360.riskengineservice.infrastructure.InMemoryRiskSignalRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class RiskEngineApplicationServiceTest {
    private final RiskEngineApplicationService service = new RiskEngineApplicationService(
            new InMemoryRiskRuleRepository(),
            new InMemoryRiskSignalRepository()
    );

    @Test
    void evaluatesFactsIntoRiskSignalAndExplanation() {
        service.createRule(new CreateRiskRuleCommand(
                "tenant-1",
                "Too many unavailable team members",
                "Leave overlap creates delivery risk",
                RiskSource.LEAVE,
                RiskSeverity.MEDIUM,
                List.of(new RiskConditionCommand("unavailableCount", RiskOperator.GREATER_THAN_OR_EQUAL, "3"))
        ));

        List<RiskSignal> signals = service.evaluate(new EvaluateRiskFactsCommand(
                "tenant-1",
                RiskSource.LEAVE,
                "team-1",
                Map.of("unavailableCount", "4")
        ));
        RiskExplanation explanation = service.explain(signals.get(0).id());

        assertEquals(1, signals.size());
        assertEquals(RiskSeverity.MEDIUM, explanation.severity());
        assertTrue(explanation.explanation().contains("Too many unavailable team members"));
    }

    @Test
    void ignoresFactsThatDoNotMatchRules() {
        service.createRule(new CreateRiskRuleCommand(
                "tenant-1",
                "Low bus factor",
                "Repository has one maintainer",
                RiskSource.DEVELOPER_OWNERSHIP,
                RiskSeverity.HIGH,
                List.of(new RiskConditionCommand("busFactor", RiskOperator.LESS_THAN, "2"))
        ));

        List<RiskSignal> signals = service.evaluate(new EvaluateRiskFactsCommand(
                "tenant-1",
                RiskSource.DEVELOPER_OWNERSHIP,
                "repo-1",
                Map.of("busFactor", "3")
        ));

        assertTrue(signals.isEmpty());
    }

    @Test
    void managesSignalLifecycle() {
        service.createRule(new CreateRiskRuleCommand(
                "tenant-1",
                "Overdue urgent ticket",
                "Helpdesk ticket is urgent and overdue",
                RiskSource.HELPDESK,
                RiskSeverity.CRITICAL,
                List.of(new RiskConditionCommand("overdueHours", RiskOperator.GREATER_THAN, "0"))
        ));
        RiskSignal signal = service.evaluate(new EvaluateRiskFactsCommand(
                "tenant-1",
                RiskSource.HELPDESK,
                "ticket-1",
                Map.of("overdueHours", "2")
        )).get(0);

        service.acknowledgeSignal(signal.id());
        RiskSignal resolved = service.resolveSignal(signal.id(), "owner-1");

        assertEquals(RiskSignalStatus.RESOLVED, resolved.status());
    }
}
