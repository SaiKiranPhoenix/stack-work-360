package com.stackwork360.workforceplanningservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.workforceplanningservice.application.WorkforcePlanningApplicationService;
import com.stackwork360.workforceplanningservice.domain.TeamCapacity;
import com.stackwork360.workforceplanningservice.domain.WorkforceCostSimulation;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workforce-planning/v1")
public class WorkforcePlanningController {
    private final WorkforcePlanningApplicationService service;

    public WorkforcePlanningController(WorkforcePlanningApplicationService service) {
        this.service = service;
    }

    @PostMapping("/headcount-plans")
    public ResponseEntity<HeadcountPlanResponse> createHeadcountPlan(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody CreateHeadcountPlanRequest request) {
        HeadcountPlanResponse response = HeadcountPlanResponse.from(service.createHeadcountPlan(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/workforce-planning/v1/headcount-plans/" + response.id())).body(response);
    }

    @GetMapping("/headcount-plans")
    public List<HeadcountPlanResponse> headcountPlans(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return service.headcountPlans(tenantId).stream().map(HeadcountPlanResponse::from).toList();
    }

    @PostMapping("/hiring-plans")
    public ResponseEntity<HiringPlanResponse> createHiringPlan(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody CreateHiringPlanRequest request) {
        HiringPlanResponse response = HiringPlanResponse.from(service.createHiringPlan(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/workforce-planning/v1/hiring-plans/" + response.id())).body(response);
    }

    @GetMapping("/hiring-plans")
    public List<HiringPlanResponse> hiringPlans(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return service.hiringPlans(tenantId).stream().map(HiringPlanResponse::from).toList();
    }

    @PostMapping("/scenarios")
    public ResponseEntity<WorkforceScenarioResponse> createScenario(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId, @Valid @RequestBody CreateScenarioRequest request) {
        WorkforceScenarioResponse response = WorkforceScenarioResponse.from(service.createScenario(request.toCommand(tenantId)));
        return ResponseEntity.created(URI.create("/api/workforce-planning/v1/scenarios/" + response.id())).body(response);
    }

    @GetMapping("/scenarios")
    public List<WorkforceScenarioResponse> scenarios(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return service.scenarios(tenantId).stream().map(WorkforceScenarioResponse::from).toList();
    }

    @GetMapping("/scenarios/{scenarioId}/simulation")
    public WorkforceCostSimulation simulate(@PathVariable UUID scenarioId) {
        return service.simulate(scenarioId);
    }

    @GetMapping("/team-capacity")
    public List<TeamCapacity> teamCapacity(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return service.teamCapacity(tenantId);
    }
}
