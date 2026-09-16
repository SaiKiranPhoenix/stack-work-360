package com.stackwork360.learningservice.infrastructure;

import com.stackwork360.learningservice.domain.RecommendationInput;
import com.stackwork360.learningservice.domain.RecommendationInputRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryRecommendationInputRepository implements RecommendationInputRepository {
    private final Map<UUID, RecommendationInput> inputs = new ConcurrentHashMap<>();

    public RecommendationInput save(RecommendationInput input) {
        inputs.put(input.id(), input);
        return input;
    }

    public List<RecommendationInput> findByWorker(String tenantId, String workerId) {
        return inputs.values().stream()
                .filter(input -> input.tenantId().equals(tenantId))
                .filter(input -> input.workerId().equals(workerId))
                .sorted(Comparator.comparing(RecommendationInput::capturedAt).reversed())
                .toList();
    }
}
