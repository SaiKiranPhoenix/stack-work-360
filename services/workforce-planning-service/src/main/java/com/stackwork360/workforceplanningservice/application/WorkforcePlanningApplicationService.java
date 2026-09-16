package com.stackwork360.workforceplanningservice.application;

import com.stackwork360.web.ResourceNotFoundException;
import com.stackwork360.workforceplanningservice.domain.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class WorkforcePlanningApplicationService {
    private final HeadcountPlanRepository headcountPlanRepository;
    private final HiringPlanRepository hiringPlanRepository;
    private final WorkforceScenarioRepository scenarioRepository;

    public WorkforcePlanningApplicationService(
            HeadcountPlanRepository headcountPlanRepository,
            HiringPlanRepository hiringPlanRepository,
            WorkforceScenarioRepository scenarioRepository
    ) {
        this.headcountPlanRepository = headcountPlanRepository;
        this.hiringPlanRepository = hiringPlanRepository;
        this.scenarioRepository = scenarioRepository;
    }

    public HeadcountPlan createHeadcountPlan(CreateHeadcountPlanCommand command) {
        return headcountPlanRepository.save(new HeadcountPlan(
                null,
                command.tenantId(),
                command.teamId(),
                command.location(),
                command.currentFullTime(),
                command.currentContractors(),
                command.targetFullTime(),
                command.targetContractors(),
                command.averageFullTimeCost(),
                command.averageContractorCost()
        ));
    }

    public HiringPlan createHiringPlan(CreateHiringPlanCommand command) {
        return hiringPlanRepository.save(new HiringPlan(null, command.tenantId(), command.teamId(), command.roleName(), command.location(), command.openings(), command.targetStartDate(), command.employmentType()));
    }

    public WorkforceScenario createScenario(CreateScenarioCommand command) {
        return scenarioRepository.save(new WorkforceScenario(null, command.tenantId(), command.name(), command.type(), command.adjustments()));
    }

    public WorkforceCostSimulation simulate(UUID scenarioId) {
        WorkforceScenario scenario = scenario(scenarioId);
        int headcountDelta = scenario.adjustments().stream().mapToInt(ScenarioAdjustment::headcountDelta).sum();
        BigDecimal costDelta = scenario.adjustments().stream()
                .map(ScenarioAdjustment::annualCostDelta)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new WorkforceCostSimulation(scenario.tenantId(), scenario.id(), headcountDelta, costDelta, scenario.adjustments());
    }

    public List<TeamCapacity> teamCapacity(String tenantId) {
        return headcountPlanRepository.findByTenantId(tenantId).stream()
                .map(TeamCapacity::from)
                .toList();
    }

    public List<HeadcountPlan> headcountPlans(String tenantId) {
        return headcountPlanRepository.findByTenantId(tenantId);
    }

    public List<HiringPlan> hiringPlans(String tenantId) {
        return hiringPlanRepository.findByTenantId(tenantId);
    }

    public List<WorkforceScenario> scenarios(String tenantId) {
        return scenarioRepository.findByTenantId(tenantId);
    }

    private WorkforceScenario scenario(UUID id) {
        return scenarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("workforce scenario not found"));
    }
}
