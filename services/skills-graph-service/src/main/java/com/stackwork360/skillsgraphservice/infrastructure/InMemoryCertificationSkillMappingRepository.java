package com.stackwork360.skillsgraphservice.infrastructure;

import com.stackwork360.skillsgraphservice.domain.CertificationSkillMapping;
import com.stackwork360.skillsgraphservice.domain.CertificationSkillMappingRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryCertificationSkillMappingRepository implements CertificationSkillMappingRepository {
    private final Map<UUID, CertificationSkillMapping> mappings = new ConcurrentHashMap<>();

    public CertificationSkillMapping save(CertificationSkillMapping mapping) {
        mappings.put(mapping.id(), mapping);
        return mapping;
    }

    public List<CertificationSkillMapping> findByTenantId(String tenantId) {
        return mappings.values().stream()
                .filter(mapping -> mapping.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(CertificationSkillMapping::certificationCode))
                .toList();
    }
}
