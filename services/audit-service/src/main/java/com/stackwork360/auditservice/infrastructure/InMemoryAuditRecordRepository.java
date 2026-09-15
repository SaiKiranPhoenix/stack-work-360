package com.stackwork360.auditservice.infrastructure;

import com.stackwork360.auditservice.domain.AuditQuery;
import com.stackwork360.auditservice.domain.AuditRecord;
import com.stackwork360.auditservice.domain.AuditRecordRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAuditRecordRepository implements AuditRecordRepository {
    private final Map<UUID, AuditRecord> records = new ConcurrentHashMap<>();

    @Override
    public AuditRecord append(AuditRecord record) {
        if (records.containsKey(record.id())) {
            throw new IllegalStateException("audit records are append-only");
        }
        records.put(record.id(), record);
        return record;
    }

    @Override
    public Optional<AuditRecord> findById(UUID id) {
        return Optional.ofNullable(records.get(id));
    }

    @Override
    public List<AuditRecord> search(AuditQuery query) {
        return records.values().stream()
                .filter(record -> record.tenantId().equals(query.tenantId()))
                .filter(record -> query.actorId() == null || record.actorId().equals(query.actorId()))
                .filter(record -> query.serviceName() == null || record.serviceName().equals(query.serviceName()))
                .filter(record -> query.resourceType() == null || record.resourceType().equals(query.resourceType()))
                .filter(record -> query.action() == null || record.action() == query.action())
                .filter(record -> query.sensitivity() == null || record.sensitivity() == query.sensitivity())
                .filter(record -> query.from() == null || !record.occurredAt().isBefore(query.from()))
                .filter(record -> query.to() == null || !record.occurredAt().isAfter(query.to()))
                .sorted(Comparator.comparing(AuditRecord::occurredAt).reversed())
                .toList();
    }
}
