package com.stackwork360.performanceservice.infrastructure;

import com.stackwork360.performanceservice.domain.ReviewTemplate;
import com.stackwork360.performanceservice.domain.ReviewTemplateRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryReviewTemplateRepository implements ReviewTemplateRepository {
    private final Map<UUID, ReviewTemplate> templates = new ConcurrentHashMap<>();

    public ReviewTemplate save(ReviewTemplate template) {
        templates.put(template.id(), template);
        return template;
    }

    public Optional<ReviewTemplate> findById(UUID id) {
        return Optional.ofNullable(templates.get(id));
    }

    public List<ReviewTemplate> findByTenantId(String tenantId) {
        return templates.values().stream()
                .filter(template -> template.tenantId().equals(tenantId))
                .sorted(Comparator.comparing(ReviewTemplate::name))
                .toList();
    }
}
