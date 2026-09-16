package com.stackwork360.goalsokrservice.api;

import com.stackwork360.goalsokrservice.domain.KeyResult;
import com.stackwork360.goalsokrservice.domain.KeyResultType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record KeyResultRequest(
        @NotBlank String title,
        @NotNull KeyResultType type,
        @NotNull BigDecimal startValue,
        @NotNull BigDecimal targetValue,
        BigDecimal currentValue
) {
    KeyResult toDomain() {
        return new KeyResult(null, title, type, startValue, targetValue, currentValue);
    }
}
