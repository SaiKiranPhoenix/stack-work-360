package com.stackwork360.benefitsservice.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.stackwork360.benefitsservice.domain.*;
import com.stackwork360.benefitsservice.infrastructure.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class BenefitsApplicationServiceTest {
    private final BenefitsApplicationService service = new BenefitsApplicationService(
            new InMemoryBenefitPlanRepository(),
            new InMemoryEligibilityRuleRepository(),
            new InMemoryOpenEnrollmentWindowRepository(),
            new InMemoryEmployeeEnrollmentRepository(),
            new InMemoryBenefitChangeRequestRepository()
    );

    @Test
    void evaluatesEligibilityAndEnrollsEmployee() {
        seedPlanAndRule();
        EmployeeProfile profile = eligibleProfile();

        EligibilityDecision decision = service.eligibility("tenant-1", profile, "MEDICAL", LocalDate.now());
        assertThat(decision.eligible()).isTrue();

        service.enroll(new EnrollEmployeeCommand("tenant-1", profile, "MEDICAL", CoverageTier.FAMILY, LocalDate.now()));

        BenefitsUsageSummary summary = service.usageSummary("tenant-1");
        assertThat(summary.activeEnrollments()).isEqualTo(1);
        assertThat(summary.monthlyEmployerCost()).isEqualByComparingTo(new BigDecimal("500.00"));
    }

    @Test
    void blocksIneligibleEmployeeEnrollment() {
        seedPlanAndRule();
        EmployeeProfile profile = new EmployeeProfile("worker-2", "CONTRACTOR", "IN", LocalDate.now().minusDays(365));

        assertThatThrownBy(() -> service.enroll(new EnrollEmployeeCommand("tenant-1", profile, "MEDICAL", CoverageTier.EMPLOYEE_ONLY, LocalDate.now())))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not eligible");
    }

    @Test
    void processesBenefitChangeDuringOpenEnrollment() {
        seedPlanAndRule();
        EmployeeProfile profile = eligibleProfile();
        service.enroll(new EnrollEmployeeCommand("tenant-1", profile, "MEDICAL", CoverageTier.EMPLOYEE_ONLY, LocalDate.now()));
        service.createOpenEnrollmentWindow(new CreateOpenEnrollmentWindowCommand("tenant-1", "FY27", LocalDate.now().minusDays(1), LocalDate.now().plusDays(15)));

        BenefitChangeRequest request = service.requestChange(new RequestBenefitChangeCommand(
                "tenant-1",
                profile,
                "MEDICAL",
                BenefitChangeType.CHANGE_TIER,
                CoverageTier.FAMILY,
                LocalDate.now().plusDays(30),
                LocalDate.now()
        ));

        service.approve(request.id(), "hr-admin");
        service.apply(request.id());

        assertThat(service.enrollments("tenant-1", profile.workerId()))
                .anySatisfy(enrollment -> assertThat(enrollment.coverageTier()).isEqualTo(CoverageTier.FAMILY));
    }

    @Test
    void requiresOpenEnrollmentForBenefitChange() {
        seedPlanAndRule();

        assertThatThrownBy(() -> service.requestChange(new RequestBenefitChangeCommand(
                "tenant-1",
                eligibleProfile(),
                "MEDICAL",
                BenefitChangeType.CHANGE_TIER,
                CoverageTier.FAMILY,
                LocalDate.now().plusDays(30),
                LocalDate.now()
        )))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("open enrollment");
    }

    private void seedPlanAndRule() {
        service.createPlan(new CreateBenefitPlanCommand("tenant-1", "MEDICAL", "Medical Plus", BenefitPlanType.HEALTH, new BigDecimal("500.00"), true));
        service.createEligibilityRule(new CreateEligibilityRuleCommand("tenant-1", "MEDICAL", "FULL_TIME", "IN", 30));
    }

    private EmployeeProfile eligibleProfile() {
        return new EmployeeProfile("worker-1", "FULL_TIME", "IN", LocalDate.now().minusDays(120));
    }
}
