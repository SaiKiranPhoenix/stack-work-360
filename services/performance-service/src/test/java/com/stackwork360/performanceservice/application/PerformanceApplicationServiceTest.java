package com.stackwork360.performanceservice.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.stackwork360.performanceservice.domain.*;
import com.stackwork360.performanceservice.infrastructure.InMemoryReviewCycleRepository;
import com.stackwork360.performanceservice.infrastructure.InMemoryReviewSubmissionRepository;
import com.stackwork360.performanceservice.infrastructure.InMemoryReviewTemplateRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class PerformanceApplicationServiceTest {
    private final PerformanceApplicationService service = new PerformanceApplicationService(
            new InMemoryReviewCycleRepository(),
            new InMemoryReviewTemplateRepository(),
            new InMemoryReviewSubmissionRepository()
    );

    @Test
    void submitsCalibratesAndCalculatesPromotionReadiness() {
        ReviewCycle cycle = service.createCycle(new CreateReviewCycleCommand("tenant-1", "FY27 H1", LocalDate.now(), LocalDate.now().plusDays(30)));
        service.activateCycle(cycle.id());

        ReviewSubmission review = service.submitReview(new SubmitReviewCommand(
                "tenant-1",
                cycle.id(),
                "worker-1",
                "manager-1",
                ReviewType.MANAGER,
                4,
                "scope expanded and outcomes landed",
                Map.of("Impact", 4, "Leadership", 4)
        ));

        service.calibrate(review.id(), new CalibrateReviewCommand(5, "calibration committee aligned on top rating"));

        PromotionReadinessSummary summary = service.promotionReadiness("tenant-1", "worker-1");
        assertThat(summary.ready()).isTrue();
        assertThat(summary.averageRating()).isEqualByComparingTo("5.00");
    }

    @Test
    void filtersVisibleReviewsByRole() {
        ReviewCycle cycle = service.createCycle(new CreateReviewCycleCommand("tenant-1", "FY27 H1", LocalDate.now(), LocalDate.now().plusDays(30)));
        service.activateCycle(cycle.id());
        ReviewSubmission managerReview = service.submitReview(new SubmitReviewCommand("tenant-1", cycle.id(), "worker-1", "manager-1", ReviewType.MANAGER, 4, "good", Map.of("Impact", 4)));
        service.makeVisible(managerReview.id());
        service.submitReview(new SubmitReviewCommand("tenant-1", cycle.id(), "worker-1", "peer-1", ReviewType.PEER, 4, "helpful", Map.of("Teamwork", 4)));

        List<ReviewSubmission> employeeVisible = service.visibleReviews("tenant-1", "worker-1", ReviewerRole.EMPLOYEE);

        assertThat(employeeVisible).extracting(ReviewSubmission::type).containsExactly(ReviewType.MANAGER);
    }
}
