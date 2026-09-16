package com.stackwork360.performanceservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReviewVisibilityPolicyTest {
    @Test
    void employeeCanOnlySeeVisibleNonPeerReviewsAboutThemself() {
        ReviewSubmission managerReview = ReviewSubmission.submit("tenant-1", UUID.randomUUID(), "worker-1", "manager-1", ReviewType.MANAGER, 4, "good", Map.of("Impact", 4));
        managerReview.makeVisible();
        ReviewSubmission peerReview = ReviewSubmission.submit("tenant-1", UUID.randomUUID(), "worker-1", "peer-1", ReviewType.PEER, 4, "good", Map.of("Teamwork", 4));

        assertThat(ReviewVisibilityPolicy.canView(managerReview, "worker-1", ReviewerRole.EMPLOYEE)).isTrue();
        assertThat(ReviewVisibilityPolicy.canView(peerReview, "worker-1", ReviewerRole.EMPLOYEE)).isFalse();
    }
}
