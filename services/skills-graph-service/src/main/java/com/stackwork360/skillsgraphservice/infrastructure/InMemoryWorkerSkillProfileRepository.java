package com.stackwork360.skillsgraphservice.infrastructure;

import com.stackwork360.skillsgraphservice.domain.WorkerSkillProfile;
import com.stackwork360.skillsgraphservice.domain.WorkerSkillProfileRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryWorkerSkillProfileRepository implements WorkerSkillProfileRepository {
    private final Map<String, WorkerSkillProfile> profiles = new ConcurrentHashMap<>();

    public WorkerSkillProfile save(WorkerSkillProfile profile) {
        profiles.put(key(profile.tenantId(), profile.workerId()), profile);
        return profile;
    }

    public Optional<WorkerSkillProfile> findByWorker(String tenantId, String workerId) {
        return Optional.ofNullable(profiles.get(key(tenantId, workerId)));
    }

    public List<WorkerSkillProfile> findByTenantId(String tenantId) {
        return profiles.values().stream()
                .filter(profile -> profile.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(WorkerSkillProfile::workerId))
                .toList();
    }

    private String key(String tenantId, String workerId) {
        return tenantId + ":" + workerId;
    }
}
