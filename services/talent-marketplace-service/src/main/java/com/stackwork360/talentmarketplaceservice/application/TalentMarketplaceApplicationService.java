package com.stackwork360.talentmarketplaceservice.application;

import com.stackwork360.talentmarketplaceservice.domain.*;
import com.stackwork360.web.ResourceNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TalentMarketplaceApplicationService {
    private final TalentOpportunityRepository opportunityRepository;
    private final InternalApplicationRepository applicationRepository;
    private final MobilityHistoryRepository historyRepository;

    public TalentMarketplaceApplicationService(
            TalentOpportunityRepository opportunityRepository,
            InternalApplicationRepository applicationRepository,
            MobilityHistoryRepository historyRepository
    ) {
        this.opportunityRepository = opportunityRepository;
        this.applicationRepository = applicationRepository;
        this.historyRepository = historyRepository;
    }

    public TalentOpportunity createOpportunity(CreateOpportunityCommand command) {
        return opportunityRepository.save(new TalentOpportunity(null, command.tenantId(), command.type(), command.title(), command.ownerId(), command.teamId(), command.requiredSkills(), command.capacity(), command.startsOn(), OpportunityStatus.DRAFT, null, null));
    }

    public TalentOpportunity openOpportunity(UUID opportunityId) {
        TalentOpportunity opportunity = opportunity(opportunityId);
        opportunity.open();
        return opportunityRepository.save(opportunity);
    }

    public InternalApplication apply(ApplyToOpportunityCommand command) {
        TalentOpportunity opportunity = opportunity(command.opportunityId());
        if (!opportunity.openForApplications()) {
            throw new IllegalStateException("opportunity is not open for applications");
        }
        return applicationRepository.save(new InternalApplication(null, command.tenantId(), command.opportunityId(), command.workerId(), command.managerId(), command.note(), ApplicationStatus.SUBMITTED, null, null, null));
    }

    public InternalApplication managerApprove(UUID applicationId, DecisionCommand command) {
        InternalApplication application = application(applicationId);
        application.managerApprove(command.reason());
        return applicationRepository.save(application);
    }

    public InternalApplication managerReject(UUID applicationId, DecisionCommand command) {
        InternalApplication application = application(applicationId);
        application.managerReject(command.reason());
        return applicationRepository.save(application);
    }

    public InternalApplication accept(UUID applicationId, DecisionCommand command) {
        InternalApplication application = application(applicationId);
        application.accept(command.reason());
        InternalApplication saved = applicationRepository.save(application);
        TalentOpportunity opportunity = opportunity(application.opportunityId());
        historyRepository.save(new MobilityHistory(null, application.tenantId(), application.workerId(), opportunity.id(), opportunity.type(), opportunity.title(), null));
        return saved;
    }

    public List<OpportunityRecommendation> recommendations(RecommendationQuery query) {
        return opportunityRepository.findByTenantId(query.tenantId()).stream()
                .filter(TalentOpportunity::openForApplications)
                .map(opportunity -> new OpportunityRecommendation(opportunity.id(), opportunity.type(), opportunity.title(), matchScore(opportunity, query.skills())))
                .filter(recommendation -> recommendation.matchScore().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(OpportunityRecommendation::matchScore).reversed())
                .toList();
    }

    public List<TalentOpportunity> opportunities(String tenantId) {
        return opportunityRepository.findByTenantId(tenantId);
    }

    public List<InternalApplication> applications(String tenantId) {
        return applicationRepository.findByTenantId(tenantId);
    }

    public List<MobilityHistory> mobilityHistory(String tenantId, String workerId) {
        return historyRepository.findByWorker(tenantId, workerId);
    }

    private BigDecimal matchScore(TalentOpportunity opportunity, List<CandidateSkill> candidateSkills) {
        long matched = opportunity.requiredSkills().stream()
                .filter(required -> candidateSkills.stream().anyMatch(skill -> skill.satisfies(required)))
                .count();
        return BigDecimal.valueOf(matched)
                .multiply(new BigDecimal("100"))
                .divide(BigDecimal.valueOf(opportunity.requiredSkills().size()), 2, RoundingMode.HALF_UP);
    }

    private TalentOpportunity opportunity(UUID id) {
        return opportunityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("opportunity not found"));
    }

    private InternalApplication application(UUID id) {
        return applicationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("application not found"));
    }
}
