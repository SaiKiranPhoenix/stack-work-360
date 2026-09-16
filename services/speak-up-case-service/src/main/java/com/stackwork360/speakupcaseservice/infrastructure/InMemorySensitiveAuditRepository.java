package com.stackwork360.speakupcaseservice.infrastructure;

import com.stackwork360.speakupcaseservice.domain.SensitiveAuditRecord;
import com.stackwork360.speakupcaseservice.domain.SensitiveAuditRepository;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemorySensitiveAuditRepository implements SensitiveAuditRepository {
    private final ConcurrentMap<UUID, SensitiveAuditRecord> recordsById = new ConcurrentHashMap<>();

    @Override
    public SensitiveAuditRecord save(SensitiveAuditRecord record) {
        recordsById.put(record.id(), record);
        return record;
    }

    @Override
    public List<SensitiveAuditRecord> findByCaseId(UUID caseId) {
        return recordsById.values().stream()
                .filter(record -> record.caseId().equals(caseId))
                .sorted(Comparator.comparing(SensitiveAuditRecord::occurredAt))
                .toList();
    }
}
