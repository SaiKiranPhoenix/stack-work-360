package com.stackwork360.talentmarketplaceservice.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.stackwork360.talentmarketplaceservice.domain.*;
import com.stackwork360.talentmarketplaceservice.infrastructure.InMemoryInternalApplicationRepository;
import com.stackwork360.talentmarketplaceservice.infrastructure.InMemoryMobilityHistoryRepository;
import com.stackwork360.talentmarketplaceservice.infrastructure.InMemoryTalentOpportunityRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class TalentMarketplaceApplicationServiceTest {
    private final TalentMarketplaceApplicationService service = new TalentMarketplaceApplicationService(
            new InMemoryTalentOpportunityRepository(),
            new InMemoryInternalApplicationRepository(),
            new InMemoryMobilityHistoryRepository()
    );

    @Test
    void ranksOpportunityRecommendationsBySkillMatch() {
        TalentOpportunity javaGig = open(opportunity("Java Backend Gig", OpportunityType.INTERNAL_GIG, List.of(new RequiredSkill("JAVA", 3), new RequiredSkill("KAFKA", 2))));
        open(opportunity("Design Mentorship", OpportunityType.MENTORSHIP, List.of(new RequiredSkill("DESIGN", 2))));

        List<OpportunityRecommendation> recommendations = service.recommendations(new RecommendationQuery("tenant-1", "worker-1", List.of(new CandidateSkill("JAVA", 4), new CandidateSkill("KAFKA", 2))));

        assertThat(recommendations).extracting(OpportunityRecommendation::opportunityId).containsExactly(javaGig.id());
        assertThat(recommendations.get(0).matchScore()).isEqualByComparingTo("100.00");
    }

    @Test
    void applicationApprovalAcceptanceCreatesMobilityHistory() {
        TalentOpportunity opportunity = open(opportunity("Payroll Project", OpportunityType.PROJECT, List.of(new RequiredSkill("JAVA", 3))));
        InternalApplication application = service.apply(new ApplyToOpportunityCommand("tenant-1", opportunity.id(), "worker-1", "manager-1", "I can help"));

        service.managerApprove(application.id(), new DecisionCommand("capacity ok"));
        service.accept(application.id(), new DecisionCommand("selected"));

        assertThat(service.mobilityHistory("tenant-1", "worker-1"))
                .singleElement()
                .satisfies(history -> {
                    assertThat(history.opportunityId()).isEqualTo(opportunity.id());
                    assertThat(history.opportunityType()).isEqualTo(OpportunityType.PROJECT);
                });
    }

    private TalentOpportunity open(TalentOpportunity opportunity) {
        return service.openOpportunity(opportunity.id());
    }

    private TalentOpportunity opportunity(String title, OpportunityType type, List<RequiredSkill> skills) {
        return service.createOpportunity(new CreateOpportunityCommand("tenant-1", type, title, "owner-1", "team-1", skills, 2, LocalDate.now().plusDays(7)));
    }
}
