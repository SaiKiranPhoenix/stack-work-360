package com.stackwork360.performanceservice.domain;

public final class ReviewVisibilityPolicy {
    private ReviewVisibilityPolicy() {
    }

    public static boolean canView(ReviewSubmission review, String actorWorkerId, ReviewerRole role) {
        if (role == ReviewerRole.HR || role == ReviewerRole.CALIBRATION_COMMITTEE) {
            return true;
        }
        if (role == ReviewerRole.MANAGER && review.type() != ReviewType.PEER) {
            return true;
        }
        if (role == ReviewerRole.PEER) {
            return review.reviewerWorkerId().equals(actorWorkerId);
        }
        return review.subjectWorkerId().equals(actorWorkerId) && review.status() == ReviewStatus.VISIBLE && review.type() != ReviewType.PEER;
    }
}
