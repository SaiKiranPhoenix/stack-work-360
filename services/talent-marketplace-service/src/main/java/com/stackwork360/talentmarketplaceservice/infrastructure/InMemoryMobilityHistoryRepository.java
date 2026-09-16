package com.stackwork360.talentmarketplaceservice.infrastructure;

import com.stackwork360.talentmarketplaceservice.domain.MobilityHistory;
import com.stackwork360.talentmarketplaceservice.domain.MobilityHistoryRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryMobilityHistoryRepository implements MobilityHistoryRepository {
    private final Map<UUID, MobilityHistory> history = new ConcurrentHashMap<>();

    public MobilityHistory save(MobilityHistory item) {
        history.put(item.id(), item);
        return item;
    }

    public List<MobilityHistory> findByWorker(String tenantId, String workerId) {
        return history.values().stream()
                .filter(item -> item.tenantId().equals(tenantId))
                .filter(item -> item.workerId().equals(workerId))
                .sorted(Comparator.comparing(MobilityHistory::recordedAt).reversed())
                .toList();
    }
}
