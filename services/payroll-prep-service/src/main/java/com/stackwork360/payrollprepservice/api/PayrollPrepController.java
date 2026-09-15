package com.stackwork360.payrollprepservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.payrollprepservice.application.AddPayrollAdjustmentCommand;
import com.stackwork360.payrollprepservice.application.AddPayrollInputCommand;
import com.stackwork360.payrollprepservice.application.CreatePayrollPeriodCommand;
import com.stackwork360.payrollprepservice.application.PayrollPrepApplicationService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payroll-prep/v1")
public class PayrollPrepController {
    private final PayrollPrepApplicationService payrollPrepApplicationService;

    public PayrollPrepController(PayrollPrepApplicationService payrollPrepApplicationService) {
        this.payrollPrepApplicationService = payrollPrepApplicationService;
    }

    @PostMapping("/periods")
    public ResponseEntity<PayrollPeriodResponse> create(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody CreatePayrollPeriodRequest request
    ) {
        PayrollPeriodResponse response = PayrollPeriodResponse.from(payrollPrepApplicationService.create(
                new CreatePayrollPeriodCommand(tenantId, request.startDate(), request.endDate())
        ));
        return ResponseEntity.created(URI.create("/api/payroll-prep/v1/periods/" + response.id()))
                .body(response);
    }

    @GetMapping("/periods")
    public List<PayrollPeriodResponse> list(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return payrollPrepApplicationService.list(tenantId).stream()
                .map(PayrollPeriodResponse::from)
                .toList();
    }

    @GetMapping("/periods/{periodId}")
    public PayrollPeriodResponse get(@PathVariable UUID periodId) {
        return PayrollPeriodResponse.from(payrollPrepApplicationService.get(periodId));
    }

    @PostMapping("/periods/{periodId}/inputs")
    public PayrollPeriodResponse addInput(
            @PathVariable UUID periodId,
            @Valid @RequestBody AddPayrollInputRequest request
    ) {
        return PayrollPeriodResponse.from(payrollPrepApplicationService.addInput(
                periodId,
                new AddPayrollInputCommand(request.workerId(), request.basePay().toDomain())
        ));
    }

    @PostMapping("/periods/{periodId}/adjustments")
    public PayrollPeriodResponse addAdjustment(
            @PathVariable UUID periodId,
            @Valid @RequestBody AddPayrollAdjustmentRequest request
    ) {
        return PayrollPeriodResponse.from(payrollPrepApplicationService.addAdjustment(
                periodId,
                new AddPayrollAdjustmentCommand(
                        request.workerId(),
                        request.type(),
                        request.amount().toDomain(),
                        request.reason()
                )
        ));
    }

    @PatchMapping("/periods/{periodId}/submit")
    public PayrollPeriodResponse submit(@PathVariable UUID periodId) {
        return PayrollPeriodResponse.from(payrollPrepApplicationService.submitForApproval(periodId));
    }

    @PatchMapping("/periods/{periodId}/approve")
    public PayrollPeriodResponse approve(@PathVariable UUID periodId) {
        return PayrollPeriodResponse.from(payrollPrepApplicationService.approve(periodId));
    }

    @PatchMapping("/periods/{periodId}/close")
    public PayrollPeriodResponse close(@PathVariable UUID periodId) {
        return PayrollPeriodResponse.from(payrollPrepApplicationService.close(periodId));
    }

    @GetMapping("/periods/{periodId}/export")
    public PayrollExportResponse export(@PathVariable UUID periodId) {
        return PayrollExportResponse.from(payrollPrepApplicationService.export(periodId));
    }

    @GetMapping("/periods/{periodId}/variance")
    public PayrollVarianceResponse variance(@PathVariable UUID periodId) {
        return PayrollVarianceResponse.from(payrollPrepApplicationService.variance(periodId));
    }
}
