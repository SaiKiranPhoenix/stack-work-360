package com.stackwork360.riskengineservice.application;

import com.stackwork360.riskengineservice.domain.RiskCondition;
import com.stackwork360.riskengineservice.domain.RiskFacts;
import com.stackwork360.riskengineservice.domain.RiskRule;
import com.stackwork360.riskengineservice.domain.RiskRuleRepository;
import com.stackwork360.riskengineservice.domain.RiskSignal;
import com.stackwork360.riskengineservice.domain.RiskSignalRepository;
import com.stackwork360.web.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class RiskEngineApplicationService {
    private final RiskRuleRepository ruleRepository;
    private final RiskSignalRepository signalRepository;

    public RiskEngineApplicationService(RiskRuleRepository ruleRepository, RiskSignalRepository signalRepository) {
        this.ruleRepository = ruleRepository;
        this.signalRepository = signalRepository;
    }

    public RiskRule createRule(CreateRiskRuleCommand command) {
        return ruleRepository.save(RiskRule.create(
                command.tenantId(),
                command.name(),
                command.description(),
                command.source(),
                command.severity(),
                conditions(command.conditions())
        ));
    }

    public RiskRule updateRule(UUID ruleId, UpdateRiskRuleCommand command) {
        RiskRule rule = getRule(ruleId);
        rule.update(
                command.name(),
                command.description(),
                command.source(),
                command.severity(),
                command.enabled(),
                conditions(command.conditions())
        );
        return ruleRepository.save(rule);
    }

    public List<RiskSignal> evaluate(EvaluateRiskFactsCommand command) {
        RiskFacts facts = new RiskFacts(command.tenantId(), command.source(), command.subjectId(), command.facts());
        return ruleRepository.findByTenantIdAndSource(command.tenantId(), command.source()).stream()
                .filter(rule -> rule.matches(facts))
                .map(rule -> signalRepository.save(rule.toSignal(facts)))
                .toList();
    }

    public RiskSignal acknowledgeSignal(UUID signalId) {
        RiskSignal signal = getSignal(signalId);
        signal.acknowledge();
        return signalRepository.save(signal);
    }

    public RiskSignal resolveSignal(UUID signalId, String actorId) {
        RiskSignal signal = getSignal(signalId);
        signal.resolve(actorId);
        return signalRepository.save(signal);
    }

    public RiskSignal dismissSignal(UUID signalId, String actorId) {
        RiskSignal signal = getSignal(signalId);
        signal.dismiss(actorId);
        return signalRepository.save(signal);
    }

    public RiskRule getRule(UUID ruleId) {
        return ruleRepository.findById(ruleId)
                .orElseThrow(() -> new ResourceNotFoundException("risk rule not found"));
    }

    public RiskSignal getSignal(UUID signalId) {
        return signalRepository.findById(signalId)
                .orElseThrow(() -> new ResourceNotFoundException("risk signal not found"));
    }

    public List<RiskRule> rules(String tenantId) {
        return ruleRepository.findByTenantId(tenantId);
    }

    public List<RiskSignal> signals(String tenantId) {
        return signalRepository.findByTenantId(tenantId);
    }

    public List<RiskSignal> subjectSignals(String tenantId, String subjectId) {
        return signalRepository.findByTenantIdAndSubjectId(tenantId, subjectId);
    }

    public RiskExplanation explain(UUID signalId) {
        RiskSignal signal = getSignal(signalId);
        return new RiskExplanation(
                signal.id(),
                signal.ruleId(),
                signal.ruleName(),
                signal.source(),
                signal.subjectId(),
                signal.severity(),
                signal.status(),
                signal.explanation(),
                signal.facts()
        );
    }

    private List<RiskCondition> conditions(List<RiskConditionCommand> commands) {
        return (commands == null ? List.<RiskConditionCommand>of() : commands).stream()
                .map(command -> new RiskCondition(command.factKey(), command.operator(), command.expectedValue()))
                .toList();
    }
}
