package com.stackwork360.documentservice.infrastructure;

import com.stackwork360.documentservice.domain.DocumentMetadata;
import com.stackwork360.documentservice.domain.DocumentRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryDocumentRepository implements DocumentRepository {
    private final ConcurrentMap<UUID, DocumentMetadata> documentsById = new ConcurrentHashMap<>();

    @Override
    public DocumentMetadata save(DocumentMetadata document) {
        documentsById.put(document.id(), document);
        return document;
    }

    @Override
    public Optional<DocumentMetadata> findById(UUID id) {
        return Optional.ofNullable(documentsById.get(id));
    }

    @Override
    public List<DocumentMetadata> findByTenantId(String tenantId) {
        return documentsById.values().stream()
                .filter(document -> document.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(DocumentMetadata::createdAt))
                .toList();
    }

    @Override
    public List<DocumentMetadata> findByTenantIdAndSubjectWorkerId(String tenantId, String workerId) {
        return documentsById.values().stream()
                .filter(document -> document.tenantId().equals(tenantId))
                .filter(document -> workerId.equals(document.subjectWorkerId()))
                .sorted(Comparator.comparing(DocumentMetadata::createdAt))
                .toList();
    }
}
