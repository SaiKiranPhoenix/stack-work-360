package com.stackwork360.skillsgraphservice.infrastructure;

import com.stackwork360.skillsgraphservice.domain.SkillEndorsement;
import com.stackwork360.skillsgraphservice.domain.SkillEndorsementRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemorySkillEndorsementRepository implements SkillEndorsementRepository {
    private final Map<UUID, SkillEndorsement> endorsements = new ConcurrentHashMap<>();

    public SkillEndorsement save(SkillEndorsement endorsement) {
        endorsements.put(endorsement.id(), endorsement);
        return endorsement;
    }

    public List<SkillEndorsement> findByWorker(String tenantId, String workerId) {
        return endorsements.values().stream()
                .filter(endorsement -> endorsement.tenantId().equals(tenantId))
                .filter(endorsement -> endorsement.workerId().equals(workerId))
                .sorted(Comparator.comparing(SkillEndorsement::endorsedAt).reversed())
                .toList();
    }
}
