package com.stackwork360.payrollprepservice.application;

import com.stackwork360.payrollprepservice.domain.Money;
import com.stackwork360.payrollprepservice.domain.PayrollAdjustment;
import com.stackwork360.payrollprepservice.domain.PayrollInput;
import com.stackwork360.payrollprepservice.domain.PayrollPeriod;
import com.stackwork360.payrollprepservice.domain.PayrollPeriodRepository;
import com.stackwork360.web.ResourceNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PayrollPrepApplicationService {
    private final PayrollPeriodRepository payrollPeriodRepository;

    public PayrollPrepApplicationService(PayrollPeriodRepository payrollPeriodRepository) {
        this.payrollPeriodRepository = payrollPeriodRepository;
    }

    public PayrollPeriod create(CreatePayrollPeriodCommand command) {
        return payrollPeriodRepository.save(PayrollPeriod.open(
                command.tenantId(),
                command.startDate(),
                command.endDate()
        ));
    }

    public PayrollPeriod addInput(UUID periodId, AddPayrollInputCommand command) {
        PayrollPeriod period = get(periodId);
        period.addInput(command.workerId(), command.basePay());
        return payrollPeriodRepository.save(period);
    }

    public PayrollPeriod addAdjustment(UUID periodId, AddPayrollAdjustmentCommand command) {
        PayrollPeriod period = get(periodId);
        period.addAdjustment(command.workerId(), new PayrollAdjustment(
                null,
                command.type(),
                command.amount(),
                command.reason()
        ));
        return payrollPeriodRepository.save(period);
    }

    public PayrollPeriod submitForApproval(UUID periodId) {
        PayrollPeriod period = get(periodId);
        period.submitForApproval();
        return payrollPeriodRepository.save(period);
    }

    public PayrollPeriod approve(UUID periodId) {
        PayrollPeriod period = get(periodId);
        period.approve();
        return payrollPeriodRepository.save(period);
    }

    public PayrollPeriod close(UUID periodId) {
        PayrollPeriod period = get(periodId);
        period.close();
        return payrollPeriodRepository.save(period);
    }

    public PayrollPeriod get(UUID periodId) {
        return payrollPeriodRepository.findById(periodId)
                .orElseThrow(() -> new ResourceNotFoundException("payroll period not found"));
    }

    public List<PayrollPeriod> list(String tenantId) {
        return payrollPeriodRepository.findByTenantId(tenantId);
    }

    public PayrollExport export(UUID periodId) {
        PayrollPeriod period = get(periodId);
        List<PayrollExportLine> lines = period.inputs().stream()
                .map(input -> new PayrollExportLine(
                        input.workerId(),
                        input.basePay(),
                        totalAdjustments(input),
                        input.grossPay()
                ))
                .toList();
        return new PayrollExport(
                period.id(),
                period.tenantId(),
                period.startDate(),
                period.endDate(),
                period.status(),
                period.totalGrossPay(),
                lines
        );
    }

    public PayrollVarianceReport variance(UUID periodId) {
        PayrollPeriod period = get(periodId);
        long adjustmentCount = period.inputs().stream()
                .mapToLong(input -> input.adjustments().size())
                .sum();
        Money totalAdjustments = period.inputs().stream()
                .map(this::totalAdjustments)
                .reduce((left, right) -> left.plus(right))
                .orElseGet(() -> Money.of("0", "USD"));
        return new PayrollVarianceReport(
                period.id(),
                period.inputs().size(),
                adjustmentCount,
                totalAdjustments,
                period.totalGrossPay()
        );
    }

    private Money totalAdjustments(PayrollInput input) {
        Money total = new Money(BigDecimal.ZERO, input.basePay().currency());
        for (PayrollAdjustment adjustment : input.adjustments()) {
            total = total.plus(adjustment.signedAmount());
        }
        return total;
    }
}
