package com.stackwork360.leaveservice.infrastructure;

import com.stackwork360.leaveservice.domain.LeaveRequest;
import com.stackwork360.leaveservice.domain.LeaveRequestRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryLeaveRequestRepository implements LeaveRequestRepository {
    private final ConcurrentMap<UUID, LeaveRequest> requestsById = new ConcurrentHashMap<>();

    @Override
    public LeaveRequest save(LeaveRequest leaveRequest) {
        requestsById.put(leaveRequest.id(), leaveRequest);
        return leaveRequest;
    }

    @Override
    public Optional<LeaveRequest> findById(UUID id) {
        return Optional.ofNullable(requestsById.get(id));
    }

    @Override
    public List<LeaveRequest> findByTenantId(String tenantId) {
        return requestsById.values().stream()
                .filter(request -> request.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(LeaveRequest::createdAt))
                .toList();
    }

    @Override
    public List<LeaveRequest> findByTenantIdAndWorkerId(String tenantId, String workerId) {
        return requestsById.values().stream()
                .filter(request -> request.tenantId().equals(tenantId))
                .filter(request -> request.workerId().equals(workerId))
                .sorted(Comparator.comparing(LeaveRequest::createdAt))
                .toList();
    }
}
