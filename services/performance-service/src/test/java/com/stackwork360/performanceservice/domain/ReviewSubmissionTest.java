package com.stackwork360.performanceservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReviewSubmissionTest {
    @Test
    void calibratesManagerReview() {
        ReviewSubmission review = managerReview();

        review.calibrate(5, "strong promotion signal");

        assertThat(review.status()).isEqualTo(ReviewStatus.CALIBRATED);
        assertThat(review.finalRating()).isEqualTo(5);
    }

    @Test
    void blocksPeerFeedbackFromEmployeeVisibility() {
        ReviewSubmission review = ReviewSubmission.submit("tenant-1", UUID.randomUUID(), "worker-1", "peer-1", ReviewType.PEER, 4, "solid partner", Map.of("Collaboration", 4));

        assertThatThrownBy(review::makeVisible)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("peer feedback");
    }

    @Test
    void enforcesRatingBounds() {
        assertThatThrownBy(() -> ReviewSubmission.submit("tenant-1", UUID.randomUUID(), "worker-1", "manager-1", ReviewType.MANAGER, 6, "too high", Map.of("Impact", 5)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("rating");
    }

    private ReviewSubmission managerReview() {
        return ReviewSubmission.submit("tenant-1", UUID.randomUUID(), "worker-1", "manager-1", ReviewType.MANAGER, 4, "high impact", Map.of("Impact", 4));
    }
}
