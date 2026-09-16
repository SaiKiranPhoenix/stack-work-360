package com.stackwork360.goalsokrservice.api;

import com.stackwork360.goalsokrservice.domain.Objective;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ObjectiveResponse(
        UUID id,
        String title,
        BigDecimal progress,
        List<KeyResultResponse> keyResults
) {
    static ObjectiveResponse from(Objective objective) {
        return new ObjectiveResponse(objective.id(), objective.title(), objective.progress().value(), objective.keyResults().stream().map(KeyResultResponse::from).toList());
    }
}
