package com.stackwork360.learningservice.infrastructure;

import com.stackwork360.learningservice.domain.LearningPath;
import com.stackwork360.learningservice.domain.LearningPathRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryLearningPathRepository implements LearningPathRepository {
    private final Map<UUID, LearningPath> paths = new ConcurrentHashMap<>();

    public LearningPath save(LearningPath path) {
        paths.put(path.id(), path);
        return path;
    }

    public List<LearningPath> findByTenantId(String tenantId) {
        return paths.values().stream()
                .filter(path -> path.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(LearningPath::title))
                .toList();
    }

    public List<LearningPath> findByRoleTarget(String tenantId, String roleTarget) {
        return paths.values().stream()
                .filter(path -> path.tenantId().equals(tenantId))
                .filter(path -> path.roleTarget().equalsIgnoreCase(roleTarget))
                .sorted(Comparator.comparing(LearningPath::title))
                .toList();
    }
}
