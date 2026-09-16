package com.stackwork360.skillsgraphservice.infrastructure;

import com.stackwork360.skillsgraphservice.domain.ProjectSkillMapping;
import com.stackwork360.skillsgraphservice.domain.ProjectSkillMappingRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryProjectSkillMappingRepository implements ProjectSkillMappingRepository {
    private final Map<UUID, ProjectSkillMapping> mappings = new ConcurrentHashMap<>();

    public ProjectSkillMapping save(ProjectSkillMapping mapping) {
        mappings.put(mapping.id(), mapping);
        return mapping;
    }

    public List<ProjectSkillMapping> findByTenantId(String tenantId) {
        return mappings.values().stream()
                .filter(mapping -> mapping.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(ProjectSkillMapping::projectCode))
                .toList();
    }
}
