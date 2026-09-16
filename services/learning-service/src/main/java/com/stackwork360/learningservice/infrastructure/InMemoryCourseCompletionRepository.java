package com.stackwork360.learningservice.infrastructure;

import com.stackwork360.learningservice.domain.CourseCompletion;
import com.stackwork360.learningservice.domain.CourseCompletionRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryCourseCompletionRepository implements CourseCompletionRepository {
    private final Map<UUID, CourseCompletion> completions = new ConcurrentHashMap<>();

    public CourseCompletion save(CourseCompletion completion) {
        completions.put(completion.id(), completion);
        return completion;
    }

    public List<CourseCompletion> findByWorker(String tenantId, String workerId) {
        return completions.values().stream()
                .filter(completion -> completion.tenantId().equals(tenantId))
                .filter(completion -> completion.workerId().equals(workerId))
                .sorted(Comparator.comparing(CourseCompletion::completedAt).reversed())
                .toList();
    }

    public boolean existsForWorkerAndResource(String tenantId, String workerId, UUID resourceId) {
        return completions.values().stream()
                .anyMatch(completion -> completion.tenantId().equals(tenantId)
                        && completion.workerId().equals(workerId)
                        && completion.resourceId().equals(resourceId));
    }
}
