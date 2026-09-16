package com.stackwork360.learningservice.application;

import java.util.List;

public record CaptureRecommendationInputCommand(String tenantId, String workerId, String roleTarget, List<String> skillGaps) {
}
