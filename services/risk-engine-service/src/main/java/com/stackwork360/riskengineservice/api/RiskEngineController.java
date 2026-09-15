package com.stackwork360.riskengineservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.riskengineservice.application.RiskEngineApplicationService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/risk-engine/v1")
public class RiskEngineController {
    private final RiskEngineApplicationService riskEngineApplicationService;

    public RiskEngineController(RiskEngineApplicationService riskEngineApplicationService) {
        this.riskEngineApplicationService = riskEngineApplicationService;
    }

    @PostMapping("/rules")
    public ResponseEntity<RiskRuleResponse> createRule(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreateRiskRuleRequest request
    ) {
        RiskRuleResponse response = RiskRuleResponse.from(riskEngineApplicationService.createRule(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/risk-engine/v1/rules/" + response.id()))
                .body(response);
    }

    @PutMapping("/rules/{ruleId}")
    public RiskRuleResponse updateRule(
            @PathVariable UUID ruleId,
            @Valid @RequestBody UpdateRiskRuleRequest request
    ) {
        return RiskRuleResponse.from(riskEngineApplicationService.updateRule(ruleId, request.toCommand()));
    }

    @GetMapping("/rules")
    public List<RiskRuleResponse> rules(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return riskEngineApplicationService.rules(tenantId).stream()
                .map(RiskRuleResponse::from)
                .toList();
    }

    @PostMapping("/evaluate")
    public List<RiskSignalResponse> evaluate(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody EvaluateRiskFactsRequest request
    ) {
        return riskEngineApplicationService.evaluate(request.toCommand(tenantId)).stream()
                .map(RiskSignalResponse::from)
                .toList();
    }

    @GetMapping("/signals")
    public List<RiskSignalResponse> signals(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam(required = false) String subjectId
    ) {
        return (subjectId == null
                ? riskEngineApplicationService.signals(tenantId)
                : riskEngineApplicationService.subjectSignals(tenantId, subjectId)).stream()
                .map(RiskSignalResponse::from)
                .toList();
    }

    @GetMapping("/signals/{signalId}")
    public RiskSignalResponse signal(@PathVariable UUID signalId) {
        return RiskSignalResponse.from(riskEngineApplicationService.getSignal(signalId));
    }

    @PatchMapping("/signals/{signalId}/acknowledge")
    public RiskSignalResponse acknowledge(@PathVariable UUID signalId) {
        return RiskSignalResponse.from(riskEngineApplicationService.acknowledgeSignal(signalId));
    }

    @PatchMapping("/signals/{signalId}/resolve")
    public RiskSignalResponse resolve(
            @PathVariable UUID signalId,
            @Valid @RequestBody RiskDecisionRequest request
    ) {
        return RiskSignalResponse.from(riskEngineApplicationService.resolveSignal(signalId, request.actorId()));
    }

    @PatchMapping("/signals/{signalId}/dismiss")
    public RiskSignalResponse dismiss(
            @PathVariable UUID signalId,
            @Valid @RequestBody RiskDecisionRequest request
    ) {
        return RiskSignalResponse.from(riskEngineApplicationService.dismissSignal(signalId, request.actorId()));
    }

    @GetMapping("/signals/{signalId}/explanation")
    public RiskExplanationResponse explain(@PathVariable UUID signalId) {
        return RiskExplanationResponse.from(riskEngineApplicationService.explain(signalId));
    }
}
