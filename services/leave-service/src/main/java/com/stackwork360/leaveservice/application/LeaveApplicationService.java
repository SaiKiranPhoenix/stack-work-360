package com.stackwork360.leaveservice.application;

import com.stackwork360.leaveservice.domain.LeaveBalance;
import com.stackwork360.leaveservice.domain.LeaveBalanceRepository;
import com.stackwork360.leaveservice.domain.LeavePolicy;
import com.stackwork360.leaveservice.domain.LeavePolicyRepository;
import com.stackwork360.leaveservice.domain.LeaveRequest;
import com.stackwork360.leaveservice.domain.LeaveRequestRepository;
import com.stackwork360.web.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class LeaveApplicationService {
    private final LeavePolicyRepository policyRepository;
    private final LeaveBalanceRepository balanceRepository;
    private final LeaveRequestRepository requestRepository;

    public LeaveApplicationService(
            LeavePolicyRepository policyRepository,
            LeaveBalanceRepository balanceRepository,
            LeaveRequestRepository requestRepository
    ) {
        this.policyRepository = policyRepository;
        this.balanceRepository = balanceRepository;
        this.requestRepository = requestRepository;
    }

    public List<LeavePolicy> policies(String tenantId) {
        return policyRepository.findByTenantId(tenantId);
    }

    public List<LeaveBalance> balances(String tenantId, String workerId) {
        return balanceRepository.findByTenantIdAndWorkerId(tenantId, workerId);
    }

    public LeaveRequest submit(SubmitLeaveRequestCommand command) {
        LeavePolicy policy = policyRepository.find(command.tenantId(), command.leaveType())
                .orElseThrow(() -> new ResourceNotFoundException("leave policy not found"));
        LeaveRequest leaveRequest = LeaveRequest.submit(
                command.tenantId(),
                command.workerId(),
                command.leaveType(),
                command.startDate(),
                command.endDate(),
                command.reason()
        );
        ensureNoConflict(leaveRequest);
        LeaveBalance balance = balanceRepository.find(command.tenantId(), command.workerId(), command.leaveType())
                .orElseGet(() -> new LeaveBalance(
                        command.tenantId(),
                        command.workerId(),
                        command.leaveType(),
                        policy.annualAllowanceDays()
                ));
        balance.reserve(leaveRequest.requestedDays());
        balanceRepository.save(balance);
        return requestRepository.save(leaveRequest);
    }

    public LeaveRequest approve(UUID leaveRequestId, String actorId) {
        LeaveRequest request = get(leaveRequestId);
        LeaveBalance balance = balanceRepository.find(request.tenantId(), request.workerId(), request.leaveType())
                .orElseThrow(() -> new ResourceNotFoundException("leave balance not found"));
        request.approve(actorId);
        balance.consumeReserved(request.requestedDays());
        balanceRepository.save(balance);
        return requestRepository.save(request);
    }

    public LeaveRequest reject(UUID leaveRequestId, String actorId) {
        LeaveRequest request = get(leaveRequestId);
        LeaveBalance balance = balanceRepository.find(request.tenantId(), request.workerId(), request.leaveType())
                .orElseThrow(() -> new ResourceNotFoundException("leave balance not found"));
        request.reject(actorId);
        balance.release(request.requestedDays());
        balanceRepository.save(balance);
        return requestRepository.save(request);
    }

    public LeaveRequest get(UUID leaveRequestId) {
        return requestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("leave request not found"));
    }

    public List<LeaveRequest> list(String tenantId) {
        return requestRepository.findByTenantId(tenantId);
    }

    public List<AvailabilityDay> availability(String tenantId, LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("end date cannot be before start date");
        }
        List<LeaveRequest> approvedRequests = requestRepository.findByTenantId(tenantId).stream()
                .filter(request -> request.activeForConflict() && !request.status().name().equals("PENDING"))
                .toList();

        return Stream.iterate(startDate, date -> !date.isAfter(endDate), date -> date.plusDays(1))
                .map(date -> new AvailabilityDay(
                        date,
                        approvedRequests.stream()
                                .filter(request -> request.overlaps(date, date))
                                .map(LeaveRequest::workerId)
                                .distinct()
                                .sorted()
                                .toList()
                ))
                .toList();
    }

    private void ensureNoConflict(LeaveRequest candidate) {
        boolean conflict = requestRepository.findByTenantIdAndWorkerId(candidate.tenantId(), candidate.workerId()).stream()
                .filter(LeaveRequest::activeForConflict)
                .anyMatch(existing -> existing.overlaps(candidate.startDate(), candidate.endDate()));
        if (conflict) {
            throw new IllegalArgumentException("leave request overlaps with an existing pending or approved request");
        }
    }
}
