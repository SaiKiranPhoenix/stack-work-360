package com.stackwork360.payrollprepservice.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.stackwork360.payrollprepservice.domain.Money;
import com.stackwork360.payrollprepservice.domain.PayrollAdjustmentType;
import com.stackwork360.payrollprepservice.domain.PayrollPeriod;
import com.stackwork360.payrollprepservice.domain.PayrollPeriodStatus;
import com.stackwork360.payrollprepservice.infrastructure.InMemoryPayrollPeriodRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PayrollPrepApplicationServiceTest {
    private final PayrollPrepApplicationService service = new PayrollPrepApplicationService(new InMemoryPayrollPeriodRepository());

    @Test
    void buildsExportAndVarianceForPreparedPeriod() {
        PayrollPeriod period = service.create(new CreatePayrollPeriodCommand(
                "tenant-1",
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 2, 28)
        ));
        service.addInput(period.id(), new AddPayrollInputCommand("worker-1", Money.of("5000", "USD")));
        service.addInput(period.id(), new AddPayrollInputCommand("worker-2", Money.of("7000", "USD")));
        service.addAdjustment(period.id(), new AddPayrollAdjustmentCommand(
                "worker-1",
                PayrollAdjustmentType.OVERTIME,
                Money.of("300", "USD"),
                "On-call support"
        ));
        service.addAdjustment(period.id(), new AddPayrollAdjustmentCommand(
                "worker-2",
                PayrollAdjustmentType.DEDUCTION,
                Money.of("100", "USD"),
                "Equipment recovery"
        ));

        PayrollExport export = service.export(period.id());
        PayrollVarianceReport variance = service.variance(period.id());

        assertEquals(2, export.lines().size());
        assertEquals(Money.of("12200", "USD"), export.totalGrossPay());
        assertEquals(2, variance.workerCount());
        assertEquals(2, variance.adjustmentCount());
        assertEquals(Money.of("200", "USD"), variance.totalAdjustments());
    }

    @Test
    void movesPeriodThroughApprovalWorkflow() {
        PayrollPeriod period = service.create(new CreatePayrollPeriodCommand(
                "tenant-1",
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 31)
        ));
        service.addInput(period.id(), new AddPayrollInputCommand("worker-1", Money.of("5000", "USD")));

        service.submitForApproval(period.id());
        service.approve(period.id());
        PayrollPeriod closed = service.close(period.id());

        assertEquals(PayrollPeriodStatus.CLOSED, closed.status());
    }
}
