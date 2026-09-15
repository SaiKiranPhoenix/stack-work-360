package com.stackwork360.shiftschedulingservice.infrastructure;

import com.stackwork360.shiftschedulingservice.domain.ShiftSwapRequest;
import com.stackwork360.shiftschedulingservice.domain.ShiftSwapRequestRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryShiftSwapRequestRepository implements ShiftSwapRequestRepository {
    private final Map<UUID, ShiftSwapRequest> requests = new ConcurrentHashMap<>();

    @Override
    public ShiftSwapRequest save(ShiftSwapRequest request) {
        requests.put(request.id(), request);
        return request;
    }

    @Override
    public Optional<ShiftSwapRequest> findById(UUID id) {
        return Optional.ofNullable(requests.get(id));
    }

    @Override
    public List<ShiftSwapRequest> findByTenantId(String tenantId) {
        return requests.values().stream()
                .filter(request -> request.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(ShiftSwapRequest::createdAt).reversed())
                .toList();
    }
}
