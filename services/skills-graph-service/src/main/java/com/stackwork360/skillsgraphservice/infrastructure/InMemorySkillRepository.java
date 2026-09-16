package com.stackwork360.skillsgraphservice.infrastructure;

import com.stackwork360.skillsgraphservice.domain.Skill;
import com.stackwork360.skillsgraphservice.domain.SkillRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemorySkillRepository implements SkillRepository {
    private final Map<String, Skill> skills = new ConcurrentHashMap<>();

    public Skill save(Skill skill) {
        skills.put(key(skill.tenantId(), skill.code()), skill);
        return skill;
    }

    public Optional<Skill> findByCode(String tenantId, String code) {
        return Optional.ofNullable(skills.get(key(tenantId, code)));
    }

    public List<Skill> findByTenantId(String tenantId) {
        return skills.values().stream()
                .filter(skill -> skill.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(Skill::code))
                .toList();
    }

    private String key(String tenantId, String code) {
        return tenantId + ":" + code;
    }
}
