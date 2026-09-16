package com.stackwork360.learningservice.infrastructure;

import com.stackwork360.learningservice.domain.Certification;
import com.stackwork360.learningservice.domain.CertificationRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryCertificationRepository implements CertificationRepository {
    private final Map<UUID, Certification> certifications = new ConcurrentHashMap<>();

    public Certification save(Certification certification) {
        certifications.put(certification.id(), certification);
        return certification;
    }

    public List<Certification> findByTenantId(String tenantId) {
        return certifications.values().stream()
                .filter(certification -> certification.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(Certification::name))
                .toList();
    }
}
