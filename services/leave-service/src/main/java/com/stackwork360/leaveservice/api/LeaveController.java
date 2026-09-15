package com.stackwork360.leaveservice.api;

import com.stackwork360.common.CorrelationIds;
import com.stackwork360.leaveservice.application.LeaveApplicationService;
import com.stackwork360.leaveservice.application.SubmitLeaveRequestCommand;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leave/v1")
public class LeaveController {
    private final LeaveApplicationService leaveApplicationService;

    public LeaveController(LeaveApplicationService leaveApplicationService) {
        this.leaveApplicationService = leaveApplicationService;
    }

    @GetMapping("/policies")
    public List<LeavePolicyResponse> policies(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return leaveApplicationService.policies(tenantId).stream()
                .map(LeavePolicyResponse::from)
                .toList();
    }

    @GetMapping("/workers/{workerId}/balances")
    public List<LeaveBalanceResponse> balances(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @PathVariable String workerId
    ) {
        return leaveApplicationService.balances(tenantId, workerId).stream()
                .map(LeaveBalanceResponse::from)
                .toList();
    }

    @PostMapping("/requests")
    public ResponseEntity<LeaveRequestResponse> submit(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @Valid @RequestBody SubmitLeaveRequest request
    ) {
        LeaveRequestResponse response = LeaveRequestResponse.from(leaveApplicationService.submit(new SubmitLeaveRequestCommand(
                tenantId,
                request.workerId(),
                request.leaveType(),
                request.startDate(),
                request.endDate(),
                request.reason()
        )));
        return ResponseEntity.created(URI.create("/api/leave/v1/requests/" + response.id()))
                .body(response);
    }

    @GetMapping("/requests")
    public List<LeaveRequestResponse> requests(@RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId) {
        return leaveApplicationService.list(tenantId).stream()
                .map(LeaveRequestResponse::from)
                .toList();
    }

    @GetMapping("/requests/{requestId}")
    public LeaveRequestResponse get(@PathVariable UUID requestId) {
        return LeaveRequestResponse.from(leaveApplicationService.get(requestId));
    }

    @PatchMapping("/requests/{requestId}/approve")
    public LeaveRequestResponse approve(
            @PathVariable UUID requestId,
            @Valid @RequestBody DecisionRequest request
    ) {
        return LeaveRequestResponse.from(leaveApplicationService.approve(requestId, request.actorId()));
    }

    @PatchMapping("/requests/{requestId}/reject")
    public LeaveRequestResponse reject(
            @PathVariable UUID requestId,
            @Valid @RequestBody DecisionRequest request
    ) {
        return LeaveRequestResponse.from(leaveApplicationService.reject(requestId, request.actorId()));
    }

    @GetMapping("/availability")
    public List<AvailabilityDayResponse> availability(
            @RequestHeader(CorrelationIds.TENANT_ID_HEADER) String tenantId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return leaveApplicationService.availability(tenantId, startDate, endDate).stream()
                .map(AvailabilityDayResponse::from)
                .toList();
    }
}
