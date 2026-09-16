package com.stackwork360.goalsokrservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class KeyResultTest {
    @Test
    void calculatesClampedProgressScore() {
        KeyResult keyResult = new KeyResult(null, "Reach 100 active customers", KeyResultType.NUMBER, BigDecimal.ZERO, new BigDecimal("100"), new BigDecimal("35"));

        assertThat(keyResult.progress().value()).isEqualByComparingTo("35.00");

        keyResult.update(new BigDecimal("150"));
        assertThat(keyResult.progress().value()).isEqualByComparingTo("100.00");
    }

    @Test
    void rejectsTargetBelowStart() {
        assertThatThrownBy(() -> new KeyResult(null, "Reduce below baseline", KeyResultType.NUMBER, new BigDecimal("10"), new BigDecimal("5"), BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("target");
    }
}
