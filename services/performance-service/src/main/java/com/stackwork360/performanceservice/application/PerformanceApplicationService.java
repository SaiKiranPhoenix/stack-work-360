package com.stackwork360.performanceservice.application;

import com.stackwork360.performanceservice.domain.*;
import com.stackwork360.web.ResourceNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PerformanceApplicationService {
    private final ReviewCycleRepository cycleRepository;
    private final ReviewTemplateRepository templateRepository;
    private final ReviewSubmissionRepository submissionRepository;

    public PerformanceApplicationService(
            ReviewCycleRepository cycleRepository,
            ReviewTemplateRepository templateRepository,
            ReviewSubmissionRepository submissionRepository
    ) {
        this.cycleRepository = cycleRepository;
        this.templateRepository = templateRepository;
        this.submissionRepository = submissionRepository;
    }

    public ReviewCycle createCycle(CreateReviewCycleCommand command) {
        return cycleRepository.save(new ReviewCycle(null, command.tenantId(), command.name(), command.startsOn(), command.endsOn(), ReviewCycleStatus.DRAFT));
    }

    public ReviewCycle activateCycle(UUID cycleId) {
        ReviewCycle cycle = cycle(cycleId);
        cycle.activate();
        return cycleRepository.save(cycle);
    }

    public ReviewCycle startCalibration(UUID cycleId) {
        ReviewCycle cycle = cycle(cycleId);
        cycle.startCalibration();
        return cycleRepository.save(cycle);
    }

    public ReviewCycle closeCycle(UUID cycleId) {
        ReviewCycle cycle = cycle(cycleId);
        cycle.close();
        return cycleRepository.save(cycle);
    }

    public ReviewTemplate createTemplate(CreateReviewTemplateCommand command) {
        return templateRepository.save(new ReviewTemplate(null, command.tenantId(), command.name(), command.type(), command.competencies(), command.active()));
    }

    public ReviewSubmission submitReview(SubmitReviewCommand command) {
        ReviewCycle cycle = cycle(command.cycleId());
        if (cycle.status() != ReviewCycleStatus.ACTIVE && cycle.status() != ReviewCycleStatus.CALIBRATION) {
            throw new IllegalStateException("reviews can only be submitted in active or calibration cycles");
        }
        return submissionRepository.save(ReviewSubmission.submit(
                command.tenantId(),
                command.cycleId(),
                command.subjectWorkerId(),
                command.reviewerWorkerId(),
                command.type(),
                command.rating(),
                command.narrative(),
                command.competencyRatings()
        ));
    }

    public ReviewSubmission calibrate(UUID reviewId, CalibrateReviewCommand command) {
        ReviewSubmission review = review(reviewId);
        ReviewCycle cycle = cycle(review.cycleId());
        if (cycle.status() == ReviewCycleStatus.ACTIVE) {
            cycle.startCalibration();
            cycleRepository.save(cycle);
        } else if (cycle.status() != ReviewCycleStatus.CALIBRATION) {
            throw new IllegalStateException("reviews can only be calibrated during calibration");
        }
        review.calibrate(command.calibratedRating(), command.notes());
        return submissionRepository.save(review);
    }

    public ReviewSubmission makeVisible(UUID reviewId) {
        ReviewSubmission review = review(reviewId);
        review.makeVisible();
        return submissionRepository.save(review);
    }

    public List<ReviewSubmission> visibleReviews(String tenantId, String actorWorkerId, ReviewerRole role) {
        return submissionRepository.findByTenantId(tenantId).stream()
                .filter(review -> ReviewVisibilityPolicy.canView(review, actorWorkerId, role))
                .toList();
    }

    public PromotionReadinessSummary promotionReadiness(String tenantId, String workerId) {
        List<ReviewSubmission> managerReviews = submissionRepository.findByWorker(tenantId, workerId).stream()
                .filter(review -> review.type() == ReviewType.MANAGER)
                .filter(review -> review.status() == ReviewStatus.CALIBRATED || review.status() == ReviewStatus.VISIBLE)
                .toList();
        if (managerReviews.isEmpty()) {
            return new PromotionReadinessSummary(tenantId, workerId, 0, BigDecimal.ZERO.setScale(2), false, "not enough manager review data");
        }
        BigDecimal average = managerReviews.stream()
                .map(review -> BigDecimal.valueOf(review.finalRating()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(managerReviews.size()), 2, RoundingMode.HALF_UP);
        boolean ready = average.compareTo(new BigDecimal("4.00")) >= 0 && managerReviews.size() >= 1;
        return new PromotionReadinessSummary(tenantId, workerId, managerReviews.size(), average, ready, ready ? "ready for promotion discussion" : "continue development plan");
    }

    public List<ReviewCycle> cycles(String tenantId) {
        return cycleRepository.findByTenantId(tenantId);
    }

    public List<ReviewTemplate> templates(String tenantId) {
        return templateRepository.findByTenantId(tenantId);
    }

    public List<ReviewSubmission> reviews(String tenantId, String workerId) {
        return workerId == null ? submissionRepository.findByTenantId(tenantId) : submissionRepository.findByWorker(tenantId, workerId);
    }

    private ReviewCycle cycle(UUID id) {
        return cycleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("review cycle not found"));
    }

    private ReviewSubmission review(UUID id) {
        return submissionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("review not found"));
    }
}
