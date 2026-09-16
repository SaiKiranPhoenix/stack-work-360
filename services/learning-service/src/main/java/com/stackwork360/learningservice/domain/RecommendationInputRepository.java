package com.stackwork360.learningservice.domain;

import java.util.List;

public interface RecommendationInputRepository {
    RecommendationInput save(RecommendationInput input);
    List<RecommendationInput> findByWorker(String tenantId, String workerId);
}
