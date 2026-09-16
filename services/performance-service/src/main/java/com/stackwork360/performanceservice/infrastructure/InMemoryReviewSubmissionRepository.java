package com.stackwork360.performanceservice.infrastructure;

import com.stackwork360.performanceservice.domain.ReviewSubmission;
import com.stackwork360.performanceservice.domain.ReviewSubmissionRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryReviewSubmissionRepository implements ReviewSubmissionRepository {
    private final Map<UUID, ReviewSubmission> submissions = new ConcurrentHashMap<>();

    public ReviewSubmission save(ReviewSubmission submission) {
        submissions.put(submission.id(), submission);
        return submission;
    }

    public Optional<ReviewSubmission> findById(UUID id) {
        return Optional.ofNullable(submissions.get(id));
    }

    public List<ReviewSubmission> findByTenantId(String tenantId) {
        return submissions.values().stream()
                .filter(submission -> submission.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(ReviewSubmission::submittedAt).reversed())
                .toList();
    }

    public List<ReviewSubmission> findByWorker(String tenantId, String workerId) {
        return submissions.values().stream()
                .filter(submission -> submission.tenantId().equals(tenantId))
                .filter(submission -> submission.subjectWorkerId().equals(workerId))
                .sorted(Comparator.comparing(ReviewSubmission::submittedAt).reversed())
                .toList();
    }

    public List<ReviewSubmission> findByCycle(String tenantId, UUID cycleId) {
        return submissions.values().stream()
                .filter(submission -> submission.tenantId().equals(tenantId))
                .filter(submission -> submission.cycleId().equals(cycleId))
                .sorted(Comparator.comparing(ReviewSubmission::submittedAt).reversed())
                .toList();
    }
}
