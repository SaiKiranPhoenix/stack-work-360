package com.stackwork360.benefitsservice.infrastructure;

import com.stackwork360.benefitsservice.domain.BenefitChangeRequest;
import com.stackwork360.benefitsservice.domain.BenefitChangeRequestRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBenefitChangeRequestRepository implements BenefitChangeRequestRepository {
    private final Map<UUID, BenefitChangeRequest> requests = new ConcurrentHashMap<>();

    public BenefitChangeRequest save(BenefitChangeRequest request) {
        requests.put(request.id(), request);
        return request;
    }

    public Optional<BenefitChangeRequest> findById(UUID id) {
        return Optional.ofNullable(requests.get(id));
    }

    public List<BenefitChangeRequest> findByTenantId(String tenantId) {
        return requests.values().stream()
                .filter(request -> request.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(BenefitChangeRequest::createdAt).reversed())
                .toList();
    }
}
