package com.stackwork360.goalsokrservice.api;

import com.stackwork360.goalsokrservice.domain.KeyResult;
import com.stackwork360.goalsokrservice.domain.KeyResultType;
import java.math.BigDecimal;
import java.util.UUID;

public record KeyResultResponse(
        UUID id,
        String title,
        KeyResultType type,
        BigDecimal startValue,
        BigDecimal targetValue,
        BigDecimal currentValue,
        BigDecimal progress
) {
    static KeyResultResponse from(KeyResult keyResult) {
        return new KeyResultResponse(keyResult.id(), keyResult.title(), keyResult.type(), keyResult.startValue(), keyResult.targetValue(), keyResult.currentValue(), keyResult.progress().value());
    }
}
