package com.stackwork360.skillsgraphservice.infrastructure;

import com.stackwork360.skillsgraphservice.domain.SkillEvidence;
import com.stackwork360.skillsgraphservice.domain.SkillEvidenceRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemorySkillEvidenceRepository implements SkillEvidenceRepository {
    private final Map<UUID, SkillEvidence> evidence = new ConcurrentHashMap<>();

    public SkillEvidence save(SkillEvidence item) {
        evidence.put(item.id(), item);
        return item;
    }

    public List<SkillEvidence> findByWorker(String tenantId, String workerId) {
        return evidence.values().stream()
                .filter(item -> item.tenantId().equals(tenantId))
                .filter(item -> item.workerId().equals(workerId))
                .sorted(Comparator.comparing(SkillEvidence::capturedAt).reversed())
                .toList();
    }
}
