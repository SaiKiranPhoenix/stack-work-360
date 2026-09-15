package com.stackwork360.payrollprepservice.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PayrollPeriodTest {
    @Test
    void calculatesGrossPayWithPositiveAndNegativeAdjustments() {
        PayrollPeriod period = period();
        period.addInput("worker-1", Money.of("5000", "USD"));

        period.addAdjustment("worker-1", new PayrollAdjustment(
                null,
                PayrollAdjustmentType.BONUS,
                Money.of("750.25", "USD"),
                "Quarter bonus"
        ));
        period.addAdjustment("worker-1", new PayrollAdjustment(
                null,
                PayrollAdjustmentType.UNPAID_LEAVE,
                Money.of("250.00", "USD"),
                "Two unpaid leave days"
        ));

        assertEquals(Money.of("5500.25", "USD"), period.totalGrossPay());
    }

    @Test
    void rejectsDuplicateWorkerInputForSamePeriod() {
        PayrollPeriod period = period();
        period.addInput("worker-1", Money.of("5000", "USD"));

        assertThrows(IllegalArgumentException.class, () -> period.addInput("worker-1", Money.of("5100", "USD")));
    }

    @Test
    void enforcesApprovalStateTransitions() {
        PayrollPeriod period = period();
        period.addInput("worker-1", Money.of("5000", "USD"));

        period.submitForApproval();
        period.approve();
        period.close();

        assertEquals(PayrollPeriodStatus.CLOSED, period.status());
        assertThrows(IllegalStateException.class, () -> period.addInput("worker-2", Money.of("4500", "USD")));
    }

    @Test
    void requiresInputsBeforeSubmission() {
        PayrollPeriod period = period();

        assertThrows(IllegalStateException.class, period::submitForApproval);
    }

    private static PayrollPeriod period() {
        return PayrollPeriod.open(
                "tenant-1",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 1, 31)
        );
    }
}
