package com.stackwork360.compensationservice.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.stackwork360.compensationservice.domain.CompensationChangeReason;
import com.stackwork360.compensationservice.domain.CompensationChangeRequest;
import com.stackwork360.compensationservice.domain.Money;
import com.stackwork360.compensationservice.infrastructure.InMemoryCompensationBenchmarkRepository;
import com.stackwork360.compensationservice.infrastructure.InMemoryCompensationChangeRequestRepository;
import com.stackwork360.compensationservice.infrastructure.InMemoryCompensationHistoryRepository;
import com.stackwork360.compensationservice.infrastructure.InMemorySalaryBandRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CompensationApplicationServiceTest {
    private final CompensationApplicationService service = new CompensationApplicationService(
            new InMemorySalaryBandRepository(),
            new InMemoryCompensationHistoryRepository(),
            new InMemoryCompensationChangeRequestRepository(),
            new InMemoryCompensationBenchmarkRepository()
    );

    @Test
    void reportsPayEquityAlertsFromBandAndBenchmarkSignals() {
        service.createSalaryBand(new CreateSalaryBandCommand("tenant-1", "L4", "IN", Money.of("100000", "USD"), Money.of("140000", "USD"), Money.of("180000", "USD")));
        service.importBenchmark(new ImportBenchmarkCommand("tenant-1", "L4", "IN", Money.of("170000", "USD"), "survey"));
        service.recordCompensation(new RecordCompensationCommand("tenant-1", "worker-1", "L4", "IN", Money.of("145000", "USD"), LocalDate.now(), "hris"));

        assertThat(service.payEquityAlerts("tenant-1"))
                .singleElement()
                .satisfies(alert -> assertThat(alert.message()).contains("market median"));
    }

    @Test
    void calculatesApprovedBudgetImpact() {
        CompensationChangeRequest request = service.requestChange(new RequestCompensationChangeCommand(
                "tenant-1",
                "worker-1",
                "L3",
                "L4",
                Money.of("100000", "USD"),
                Money.of("125000", "USD"),
                LocalDate.now().plusDays(30),
                CompensationChangeReason.PROMOTION
        ));

        service.approve(request.id(), "manager-1");

        BudgetImpactReport report = service.budgetImpact("tenant-1");
        assertThat(report.approvedChangeCount()).isEqualTo(1);
        assertThat(report.annualizedImpact()).isEqualTo(Money.of("25000", "USD"));
        assertThat(report.requestIds()).containsExactly(request.id().toString());
    }
}
