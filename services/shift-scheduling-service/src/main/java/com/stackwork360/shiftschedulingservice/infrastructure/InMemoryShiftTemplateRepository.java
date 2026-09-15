package com.stackwork360.shiftschedulingservice.infrastructure;

import com.stackwork360.shiftschedulingservice.domain.ShiftTemplate;
import com.stackwork360.shiftschedulingservice.domain.ShiftTemplateRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryShiftTemplateRepository implements ShiftTemplateRepository {
    private final Map<UUID, ShiftTemplate> templates = new ConcurrentHashMap<>();

    @Override
    public ShiftTemplate save(ShiftTemplate template) {
        templates.put(template.id(), template);
        return template;
    }

    @Override
    public Optional<ShiftTemplate> findById(UUID id) {
        return Optional.ofNullable(templates.get(id));
    }

    @Override
    public List<ShiftTemplate> findByTenantId(String tenantId) {
        return templates.values().stream()
                .filter(template -> template.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(ShiftTemplate::name))
                .toList();
    }
}
