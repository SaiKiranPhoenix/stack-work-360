package com.stackwork360.workforceplanningservice.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.stackwork360.workforceplanningservice.domain.*;
import com.stackwork360.workforceplanningservice.infrastructure.InMemoryHeadcountPlanRepository;
import com.stackwork360.workforceplanningservice.infrastructure.InMemoryHiringPlanRepository;
import com.stackwork360.workforceplanningservice.infrastructure.InMemoryWorkforceScenarioRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class WorkforcePlanningApplicationServiceTest {
    private final WorkforcePlanningApplicationService service = new WorkforcePlanningApplicationService(
            new InMemoryHeadcountPlanRepository(),
            new InMemoryHiringPlanRepository(),
            new InMemoryWorkforceScenarioRepository()
    );

    @Test
    void simulatesLocationExpansionScenario() {
        WorkforceScenario scenario = service.createScenario(new CreateScenarioCommand("tenant-1", "Bangalore expansion", ScenarioType.LOCATION_EXPANSION, List.of(
                new ScenarioAdjustment("team-1", "IN-BLR", 5, 1, new BigDecimal("650000"))
        )));

        WorkforceCostSimulation simulation = service.simulate(scenario.id());

        assertThat(simulation.headcountDelta()).isEqualTo(6);
        assertThat(simulation.annualCostDelta()).isEqualByComparingTo("650000.00");
    }

    @Test
    void simulatesContractorConversionScenario() {
        WorkforceScenario scenario = service.createScenario(new CreateScenarioCommand("tenant-1", "Convert contractors", ScenarioType.CONTRACTOR_CONVERSION, List.of(
                new ScenarioAdjustment("team-1", "US", 2, -2, new BigDecimal("80000"))
        )));

        WorkforceCostSimulation simulation = service.simulate(scenario.id());

        assertThat(simulation.headcountDelta()).isZero();
        assertThat(simulation.annualCostDelta()).isEqualByComparingTo("80000.00");
    }

    @Test
    void simulatesRestructuringScenarioAndCapacity() {
        service.createHeadcountPlan(new CreateHeadcountPlanCommand("tenant-1", "team-1", "US", 8, 2, 6, 1, new BigDecimal("140000"), new BigDecimal("100000")));
        WorkforceScenario scenario = service.createScenario(new CreateScenarioCommand("tenant-1", "Flatten org", ScenarioType.RESTRUCTURING, List.of(
                new ScenarioAdjustment("team-1", "US", -2, -1, new BigDecimal("-380000"))
        )));

        WorkforceCostSimulation simulation = service.simulate(scenario.id());

        assertThat(simulation.headcountDelta()).isEqualTo(-3);
        assertThat(service.teamCapacity("tenant-1")).singleElement()
                .satisfies(capacity -> assertThat(capacity.weeklyCapacityHours()).isEqualByComparingTo("224.00"));
    }

    @Test
    void createsHiringPlan() {
        HiringPlan plan = service.createHiringPlan(new CreateHiringPlanCommand("tenant-1", "team-1", "Backend Engineer", "US", 3, LocalDate.now().plusDays(30), EmploymentType.FULL_TIME));

        assertThat(plan.openings()).isEqualTo(3);
        assertThat(service.hiringPlans("tenant-1")).containsExactly(plan);
    }
}
