package com.stackwork360.benefitsservice.application;

import com.stackwork360.benefitsservice.domain.*;
import com.stackwork360.web.ResourceNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BenefitsApplicationService {
    private final BenefitPlanRepository planRepository;
    private final EligibilityRuleRepository eligibilityRuleRepository;
    private final OpenEnrollmentWindowRepository windowRepository;
    private final EmployeeEnrollmentRepository enrollmentRepository;
    private final BenefitChangeRequestRepository changeRequestRepository;

    public BenefitsApplicationService(
            BenefitPlanRepository planRepository,
            EligibilityRuleRepository eligibilityRuleRepository,
            OpenEnrollmentWindowRepository windowRepository,
            EmployeeEnrollmentRepository enrollmentRepository,
            BenefitChangeRequestRepository changeRequestRepository
    ) {
        this.planRepository = planRepository;
        this.eligibilityRuleRepository = eligibilityRuleRepository;
        this.windowRepository = windowRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.changeRequestRepository = changeRequestRepository;
    }

    public BenefitPlan createPlan(CreateBenefitPlanCommand command) {
        return planRepository.save(new BenefitPlan(null, command.tenantId(), command.planCode(), command.name(), command.type(), command.monthlyEmployerCost(), command.active()));
    }

    public EligibilityRule createEligibilityRule(CreateEligibilityRuleCommand command) {
        plan(command.tenantId(), command.planCode());
        return eligibilityRuleRepository.save(new EligibilityRule(null, command.tenantId(), command.planCode(), command.employmentType(), command.region(), command.minimumTenureDays()));
    }

    public OpenEnrollmentWindow createOpenEnrollmentWindow(CreateOpenEnrollmentWindowCommand command) {
        return windowRepository.save(new OpenEnrollmentWindow(null, command.tenantId(), command.name(), command.startsOn(), command.endsOn()));
    }

    public EligibilityDecision eligibility(String tenantId, EmployeeProfile profile, String planCode, LocalDate asOf) {
        BenefitPlan plan = plan(tenantId, planCode);
        if (!plan.active()) {
            return new EligibilityDecision(tenantId, profile.workerId(), planCode, false, List.of("plan is inactive"));
        }
        List<EligibilityRule> rules = eligibilityRuleRepository.findByPlanCode(tenantId, planCode);
        if (rules.isEmpty()) {
            return new EligibilityDecision(tenantId, profile.workerId(), planCode, true, List.of("no restrictive eligibility rules"));
        }
        boolean eligible = rules.stream().anyMatch(rule -> rule.eligible(profile, asOf));
        return eligible
                ? new EligibilityDecision(tenantId, profile.workerId(), planCode, true, List.of("matched eligibility rule"))
                : new EligibilityDecision(tenantId, profile.workerId(), planCode, false, List.of("no eligibility rule matched"));
    }

    public EmployeeEnrollment enroll(EnrollEmployeeCommand command) {
        EligibilityDecision decision = eligibility(command.tenantId(), command.profile(), command.planCode(), command.coverageStart());
        if (!decision.eligible()) {
            throw new IllegalStateException("worker is not eligible for benefit plan");
        }
        return enrollmentRepository.save(EmployeeEnrollment.active(command.tenantId(), command.profile().workerId(), command.planCode(), command.coverageTier(), command.coverageStart()));
    }

    public BenefitChangeRequest requestChange(RequestBenefitChangeCommand command) {
        if (windowRepository.activeWindow(command.tenantId(), command.requestedOn()).isEmpty()) {
            throw new IllegalStateException("benefit changes require an active open enrollment window");
        }
        EligibilityDecision decision = eligibility(command.tenantId(), command.profile(), command.planCode(), command.requestedOn());
        if (!decision.eligible()) {
            throw new IllegalStateException("worker is not eligible for benefit plan");
        }
        return changeRequestRepository.save(BenefitChangeRequest.request(command.tenantId(), command.profile().workerId(), command.planCode(), command.type(), command.requestedTier(), command.effectiveDate()));
    }

    public BenefitChangeRequest approve(UUID requestId, String actorId) {
        BenefitChangeRequest request = changeRequest(requestId);
        request.approve(actorId);
        return changeRequestRepository.save(request);
    }

    public BenefitChangeRequest reject(UUID requestId, String actorId) {
        BenefitChangeRequest request = changeRequest(requestId);
        request.reject(actorId);
        return changeRequestRepository.save(request);
    }

    public BenefitChangeRequest apply(UUID requestId) {
        BenefitChangeRequest request = changeRequest(requestId);
        request.apply();
        applyEnrollmentChange(request);
        return changeRequestRepository.save(request);
    }

    public BenefitsUsageSummary usageSummary(String tenantId) {
        List<EmployeeEnrollment> enrollments = enrollmentRepository.findByTenantId(tenantId);
        int active = (int) enrollments.stream().filter(enrollment -> enrollment.status() == EnrollmentStatus.ACTIVE).count();
        int waived = (int) enrollments.stream().filter(enrollment -> enrollment.status() == EnrollmentStatus.WAIVED).count();
        BigDecimal monthlyCost = enrollments.stream()
                .filter(enrollment -> enrollment.status() == EnrollmentStatus.ACTIVE)
                .map(enrollment -> planRepository.findByCode(tenantId, enrollment.planCode())
                        .map(BenefitPlan::monthlyEmployerCost)
                        .orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new BenefitsUsageSummary(tenantId, active, waived, monthlyCost);
    }

    public List<BenefitPlan> plans(String tenantId) {
        return planRepository.findByTenantId(tenantId);
    }

    public List<EligibilityRule> eligibilityRules(String tenantId) {
        return eligibilityRuleRepository.findByTenantId(tenantId);
    }

    public List<OpenEnrollmentWindow> openEnrollmentWindows(String tenantId) {
        return windowRepository.findByTenantId(tenantId);
    }

    public List<EmployeeEnrollment> enrollments(String tenantId, String workerId) {
        return workerId == null ? enrollmentRepository.findByTenantId(tenantId) : enrollmentRepository.findByWorker(tenantId, workerId);
    }

    public List<BenefitChangeRequest> changeRequests(String tenantId) {
        return changeRequestRepository.findByTenantId(tenantId);
    }

    private BenefitPlan plan(String tenantId, String planCode) {
        return planRepository.findByCode(tenantId, planCode).orElseThrow(() -> new ResourceNotFoundException("benefit plan not found"));
    }

    private BenefitChangeRequest changeRequest(UUID id) {
        return changeRequestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("benefit change request not found"));
    }

    private void applyEnrollmentChange(BenefitChangeRequest request) {
        if (request.type() == BenefitChangeType.CANCEL) {
            enrollmentRepository.findActive(request.tenantId(), request.workerId(), request.planCode())
                    .ifPresent(enrollment -> enrollmentRepository.save(enrollment.cancelled(request.effectiveDate())));
            return;
        }
        enrollmentRepository.findActive(request.tenantId(), request.workerId(), request.planCode())
                .ifPresent(enrollment -> enrollmentRepository.save(enrollment.cancelled(request.effectiveDate())));
        CoverageTier tier = request.type() == BenefitChangeType.WAIVE ? CoverageTier.WAIVED : request.requestedTier();
        enrollmentRepository.save(EmployeeEnrollment.active(request.tenantId(), request.workerId(), request.planCode(), tier, request.effectiveDate()));
    }
}
