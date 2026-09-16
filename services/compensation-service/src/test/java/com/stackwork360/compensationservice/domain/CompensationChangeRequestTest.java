package com.stackwork360.compensationservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CompensationChangeRequestTest {
    @Test
    void calculatesBudgetImpact() {
        CompensationChangeRequest request = request();

        assertThat(request.budgetImpact()).isEqualTo(Money.of("20000", "USD"));
    }

    @Test
    void detectsPromotionCompensationWorkflow() {
        CompensationChangeRequest request = request();

        assertThat(request.promotion()).isTrue();
    }

    @Test
    void requiresApprovalBeforeApply() {
        CompensationChangeRequest request = request();

        assertThatThrownBy(request::apply)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("approved");
    }

    private CompensationChangeRequest request() {
        return CompensationChangeRequest.request(
                "tenant-1",
                "worker-1",
                "L3",
                "L4",
                Money.of("100000", "USD"),
                Money.of("120000", "USD"),
                LocalDate.now().plusDays(30),
                CompensationChangeReason.PROMOTION
        );
    }
}
