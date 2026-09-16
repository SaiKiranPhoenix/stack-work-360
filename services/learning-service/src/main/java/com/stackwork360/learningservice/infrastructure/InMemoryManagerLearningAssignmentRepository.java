package com.stackwork360.learningservice.infrastructure;

import com.stackwork360.learningservice.domain.ManagerLearningAssignment;
import com.stackwork360.learningservice.domain.ManagerLearningAssignmentRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryManagerLearningAssignmentRepository implements ManagerLearningAssignmentRepository {
    private final Map<UUID, ManagerLearningAssignment> assignments = new ConcurrentHashMap<>();

    public ManagerLearningAssignment save(ManagerLearningAssignment assignment) {
        assignments.put(assignment.id(), assignment);
        return assignment;
    }

    public Optional<ManagerLearningAssignment> findById(UUID id) {
        return Optional.ofNullable(assignments.get(id));
    }

    public List<ManagerLearningAssignment> findByWorker(String tenantId, String workerId) {
        return assignments.values().stream()
                .filter(assignment -> assignment.tenantId().equals(tenantId))
                .filter(assignment -> assignment.workerId().equals(workerId))
                .sorted(Comparator.comparing(ManagerLearningAssignment::dueOn))
                .toList();
    }
}
